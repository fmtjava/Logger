package com.fmt.core.log.strategy

import com.fmt.core.log.LogModel
import com.fmt.core.log.DiskSizeUnit
import com.fmt.core.log.formatter.Formatter
import com.fmt.core.log.formatter.DefaultFormatter
import com.fmt.core.log.strategy.writer.WriteFactory
import com.fmt.core.log.strategy.writer.Writer
import com.fmt.core.log.strategy.writer.WriterStrategy
import java.io.File
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue

/**
 * BIO方法日志写入磁盘
 */
class DiskLogStrategy private constructor(private val builder: Builder) :
    LogStrategy {

    private var worker: PrintWorker? = null

    init {
        worker = PrintWorker()
    }

    companion object {
        val EXECUTOR: ExecutorService = Executors.newSingleThreadExecutor()

        fun newBuilder(): Builder = Builder()
    }

    override fun log(priority: Int, tag: String, message: String) {
        if (worker?.isRunning() == false) {
            worker?.start()
        }
        worker?.put(LogModel(priority, tag, message))
    }

    class Builder {
        internal var diskDirPath: String? = null //指定输出目录文件的路径
        internal var maxDiskDirSize = 1024 * 1024 * 100L //指定输出目录文件的最大存储空间
        internal var maxDiskFileSize = 1024 * 1024L //指定单个日志文件的大小
        internal var writerStrategy = WriterStrategy.BIO
        internal var writer: Writer? = null
        internal var formatter: Formatter = DefaultFormatter()

        fun diskDirPath(diskDirPath: String): Builder {
            this.diskDirPath = diskDirPath
            return this
        }

        fun maxDiskDirSize(maxDiskDirSize: Long, diskSizeUnit: DiskSizeUnit): Builder {
            this.maxDiskDirSize = diskSizeUnit.toB(maxDiskDirSize)
            return this
        }

        fun maxDiskFileSize(maxDiskFileSize: Long, diskSizeUnit: DiskSizeUnit): Builder {
            this.maxDiskFileSize = diskSizeUnit.toB(maxDiskFileSize)
            return this
        }

        fun writerStrategy(writerStrategy: WriterStrategy): Builder {
            this.writerStrategy = writerStrategy
            return this
        }

        fun formatter(formatter: Formatter): Builder {
            this.formatter = formatter
            return this
        }

        fun build(): DiskLogStrategy {
            check(!diskDirPath.isNullOrEmpty()) {
                "必须设置diskDirPath参数，用于日志保存目录使用"
            }
            val folderFile = File(diskDirPath!!)
            if (!folderFile.exists()) {
                folderFile.mkdirs()
            }
            check(folderFile.exists()) {
                "$folderFile 路径不存在，请重新指定合法路径"
            }

            check(maxDiskFileSize <= maxDiskDirSize) {
                "单个文件的大小不能超过日志目录存储总大小"
            }
            writer =
                WriteFactory.create(writerStrategy, diskDirPath!!, maxDiskDirSize, maxDiskFileSize)
            return DiskLogStrategy(this)
        }
    }

    private fun doPrint(logMo: LogModel) {
        builder.writer?.writeLogToDisk(builder.formatter.format(logMo.level,
            logMo.tag,
            logMo.log))
    }

    inner class PrintWorker : Runnable {

        private val logs: BlockingQueue<LogModel> = LinkedBlockingQueue()

        @Volatile
        private var running = false

        internal fun put(logMo: LogModel) {
            try {
                logs.put(logMo)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        fun isRunning(): Boolean {
            synchronized(this) { return running }
        }

        fun start() {
            synchronized(this) {
                EXECUTOR.execute(this)
                running = true
            }
        }

        override fun run() {
            var logMo: LogModel?
            try {
                while (true) {
                    logMo = logs.take()
                    doPrint(logMo)
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
                synchronized(this) { running = false }
            }
        }
    }
}