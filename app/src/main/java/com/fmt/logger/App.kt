package com.fmt.logger

import android.app.Application
import com.fmt.core.log.DiskSizeUnit
import com.fmt.core.log.LogConfiguration
import com.fmt.core.log.LogLevel
import com.fmt.core.log.Logger
import com.fmt.core.log.adapter.AndroidLogAdapter
import com.fmt.core.log.adapter.DiskLogAdapter
import com.fmt.core.log.strategy.DiskLogStrategy
import com.fmt.core.log.strategy.writer.WriterStrategy

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Logger.init(LogConfiguration.Builder()
            .logLevel(LogLevel.ALL)                                   //指定日志输出级别
            .addAdapter(AndroidLogAdapter(isLoggable = true))           // 添加控制台日志输出策略
            .addAdapter(DiskLogAdapter(DiskLogStrategy.Builder()        // 添加磁盘日志输出策略
                .diskDirPath(getExternalFilesDir(null)?.absolutePath + "/log")
                .maxDiskDirSize(500, DiskSizeUnit.MB)       // 指定日志文件夹的最大存储大小
                .maxDiskFileSize(2, DiskSizeUnit.MB)        // 指定单个文件的最大存储大小
                .writerStrategy(WriterStrategy.NATIVE)                    // 写入策略，分为 BIO、NIO、Native
                .build(), isLoggable = true))
            .build())
    }
}