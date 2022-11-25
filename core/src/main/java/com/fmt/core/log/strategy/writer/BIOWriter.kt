package com.fmt.core.log.strategy.writer

import java.io.File
import java.io.FileWriter
import java.util.*

internal class BIOWriter : Writer() {

    private var mCurrentFile: File? = null
    private var mCurrentFileWriter: FileWriter? = null

    override fun writeLogToDisk(content: String) {
        try {
            mCurrentFile = getLogFile(folder, content)
            mCurrentFileWriter?.append(content)
            mCurrentFileWriter?.flush()
        } catch (e: Exception) {
            //
        }
    }

    @Throws(Exception::class)
    private fun getLogFile(folder: String, content: String): File {
        val contentSize = content.toByteArray().size

        if (mCurrentFile != null && mCurrentFile!!.exists() && mCurrentFile!!.length() + contentSize <= maxDiskFileSize) {
            return mCurrentFile!!
        }
        val fileName = mDateFormat.format(Date())
        mCurrentFile = File(folder, "$fileName.txt")
        mCurrentFileWriter?.close()
        mCurrentFileWriter = FileWriter(mCurrentFile, true)

        adjustFolderSizeAsync()

        return mCurrentFile!!
    }
}