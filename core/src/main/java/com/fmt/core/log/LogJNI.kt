package com.fmt.core.log

class LogJNI {

    companion object {
        init {
            System.loadLibrary("logger-lib")
        }
    }

    @Throws(Exception::class)
    external fun mMapOpen(filePath: String, maxDiskFileSize: Long)

    @Throws(Exception::class)
    external fun mMapClose()

    @Throws(Exception::class)
    external fun mMapWrite(content: String): Long

}