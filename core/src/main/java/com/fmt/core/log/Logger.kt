package com.fmt.core.log

/**
 * 日志门面类
 */
object Logger {

    private val delegate by lazy { LoggerDelegate(LogConfiguration.Builder().build()) }

    fun v(tag: String, message: String, vararg args: Any?) {
        delegate.v(tag, message, *args)
    }

    fun d(tag: String, message: String, vararg args: Any?) {
        delegate.d(tag, message, *args)
    }

    fun i(tag: String, message: String, vararg args: Any?) {
        delegate.i(tag, message, *args)
    }

    fun w(tag: String, message: String, vararg args: Any?) {
        delegate.w(tag, message, *args)
    }

    fun e(tag: String, message: String, vararg args: Any?) {
        delegate.e(tag, message, *args)
    }

    fun wtf(tag: String, message: String, vararg args: Any?) {
        delegate.wtf(tag, message, *args)
    }

    fun init(logConfiguration: LogConfiguration) {
        delegate.logConfiguration = logConfiguration
    }

}