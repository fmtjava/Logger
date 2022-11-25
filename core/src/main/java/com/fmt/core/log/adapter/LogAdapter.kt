package com.fmt.core.log.adapter

/**
 * 抽象日志打印适配器
 */
interface LogAdapter {

    /**
     * 是否启动该适配器
     */
    fun isLoggable(priority: Int, tag: String): Boolean

    /**
     * 具体适配器的打印逻辑
     */
    fun log(level: Int, tag: String, message: String)
}