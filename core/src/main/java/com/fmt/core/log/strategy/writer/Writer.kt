package com.fmt.core.log.strategy.writer

import java.io.File
import java.text.SimpleDateFormat

internal abstract class Writer {

    var folder: String = ""
    private var maxDiskDirSize: Long = 0L
    var maxDiskFileSize = 0L

    internal val mDateFormat: SimpleDateFormat by lazy { SimpleDateFormat("yyyy-MM-dd-HH-mm-ss.SSS") }

    //若新写入的日志大小 + 当前日志文件已使用的大小 >= 日志文件夹最大的容量，则删除最早写入的日志文件
    internal fun adjustFolderSizeAsync() {
        val folderFile = File(folder)
        val fileList: List<File>? = folderFile.listFiles()?.sortedBy { it.lastModified() }
        val exceedFileList = mutableListOf<File>()

        fileList?.let {
            var size: Long = 0
            for (file in fileList) {
                while ((size + file.length()) > maxDiskDirSize) {
                    val exceedFile = fileList[exceedFileList.size]
                    exceedFileList.add(exceedFile)
                    size -= exceedFile.length()
                }
                size += file.length()
            }
        }
        for (file in exceedFileList) {
            file.delete()
        }
    }

    internal fun initNecessaryParam(folder: String, maxDiskDirSize: Long, maxDiskFileSize: Long) {
        this.folder = folder
        this.maxDiskDirSize = maxDiskDirSize
        this.maxDiskFileSize = maxDiskFileSize
    }

    abstract fun writeLogToDisk(content: String)
}