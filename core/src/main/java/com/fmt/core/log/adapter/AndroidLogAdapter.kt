package com.fmt.core.log.adapter

import com.fmt.core.log.strategy.LogStrategy
import com.fmt.core.log.strategy.LogcatLogStrategy

/**
 * Android 控制台打印适配器
 */
class AndroidLogAdapter(
    private val isLoggable: Boolean = true,
) : LogAdapter {

    private val logStrategy: LogStrategy = LogcatLogStrategy()

    override fun isLoggable(priority: Int, tag: String): Boolean = isLoggable

    override fun log(level: Int, tag: String, message: String) {
        logStrategy.log(level, tag, message)
    }
}