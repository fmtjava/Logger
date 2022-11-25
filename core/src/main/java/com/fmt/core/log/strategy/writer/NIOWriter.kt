package com.fmt.core.log.strategy.writer

import android.annotation.SuppressLint
import java.io.File
import java.io.RandomAccessFile
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.util.*

internal class NIOWriter : Writer() {

    private var mRandomAccessFile: RandomAccessFile? = null
    private var mFileChannel: FileChannel? = null
    private var mMappedByteBuffer: MappedByteBuffer? = null

    override fun writeLogToDisk(content: String) {
        try {
            val contentBytes = content.toByteArray()
            val contentSize = contentBytes.size
            writeLog(contentBytes, contentSize)
        } catch (e: Exception) {
           //
        }
    }


    @Throws(Exception::class)
    fun writeLog(contentBytes: ByteArray, contentSize: Int) {
        if (mMappedByteBuffer != null && mMappedByteBuffer!!.position() + contentSize <= mMappedByteBuffer!!.capacity()) {
            mMappedByteBuffer?.put(contentBytes)
            return
        }

        mRandomAccessFile?.close()
        mFileChannel?.close()
        mMappedByteBuffer?.unmap()

        val fileName = mDateFormat.format(Date())
        val file = File(folder, "$fileName.txt")
        mRandomAccessFile = RandomAccessFile(file, "rw")
        mFileChannel = mRandomAccessFile!!.channel
        mMappedByteBuffer =
            mFileChannel!!.map(FileChannel.MapMode.READ_WRITE, 0, maxDiskFileSize)

        adjustFolderSizeAsync()
    }

    /**
     * 解除内存与文件的映射
     * */
    @SuppressLint("BlockedPrivateApi")
    private fun MappedByteBuffer.unmap() {
        try {
            val clazz = Class.forName("sun.nio.ch.FileChannelImpl")
            val m = clazz.getDeclaredMethod(
                "unmap", MappedByteBuffer::class.java
            )
            m.isAccessible = true
            m.invoke(null, this)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}