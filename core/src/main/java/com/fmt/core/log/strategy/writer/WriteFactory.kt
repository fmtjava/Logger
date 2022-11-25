package com.fmt.core.log.strategy.writer

internal object WriteFactory {

    fun create(
        strategy: WriterStrategy,
        folder: String,
        maxDiskDirSize: Long,
        maxDiskFileSize: Long,
    ): Writer = when (strategy) {
        WriterStrategy.BIO -> {
            BIOWriter().apply {
                initNecessaryParam(folder, maxDiskDirSize, maxDiskFileSize)
            }
        }
        WriterStrategy.NIO -> {
            NIOWriter().apply {
                initNecessaryParam(folder, maxDiskDirSize, maxDiskFileSize)
            }
        }
        WriterStrategy.NATIVE -> {
            NativeWriter().apply {
                initNecessaryParam(folder, maxDiskDirSize, maxDiskFileSize)
            }
        }
    }
}