package com.fmt.core.log.strategy

/**
 * 重写日志打印策略
 */
interface LogStrategy {
    fun log(priority: Int, tag: String, message: String)
}