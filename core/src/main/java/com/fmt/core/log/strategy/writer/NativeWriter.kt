package com.fmt.core.log.strategy.writer

import com.fmt.core.log.LogJNI
import java.io.File
import java.util.*

internal class NativeWriter : Writer() {

    private val jni by lazy { LogJNI() }
    private var mCurrentFileSize = 0L
    private var mCurrentFile: File? = null

    override fun writeLogToDisk(content: String) {
        try {
            mCurrentFile = getLogFile(folder, content)
            mCurrentFileSize = jni.mMapWrite(content)
        } catch (e: Exception) {
            //
        }
    }

    @Throws(Exception::class)
    private fun getLogFile(folder: String, content: String): File {
        val contentSize = content.toByteArray().size

        if (mCurrentFile != null && mCurrentFile!!.exists() && mCurrentFileSize + contentSize <= maxDiskFileSize) {
            return mCurrentFile!!
        }
        jni.mMapClose()
        val fileName = mDateFormat.format(Date())
        mCurrentFile = File(folder, "$fileName.txt")
        jni.mMapOpen(mCurrentFile!!.absolutePath, maxDiskFileSize)
        mCurrentFileSize = 0
        adjustFolderSizeAsync()

        return mCurrentFile!!
    }
}