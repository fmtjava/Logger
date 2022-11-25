package com.fmt.core.log.adapter

import com.fmt.core.log.strategy.DiskLogStrategy

/**
 * 磁盘日志写入适配器
 */
class DiskLogAdapter(
    private var logStrategy: DiskLogStrategy,
    private val isLoggable: Boolean = true,
) : LogAdapter {

    override fun isLoggable(priority: Int, tag: String): Boolean = isLoggable

    override fun log(level: Int, tag: String, message: String) {
        logStrategy.log(level, tag, message)
    }

}