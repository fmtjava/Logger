package com.fmt.core.log

/**
 * 日志代理类
 */
internal class LoggerDelegate(internal var logConfiguration: LogConfiguration) {

    fun v(tag: String, message: String, vararg args: Any?) {
        log(LogLevel.VERBOSE, tag, message, *args)
    }

    fun d(tag: String, message: String, vararg args: Any?) {
        log(LogLevel.DEBUG, tag, message, *args)
    }

    fun i(tag: String, message: String, vararg args: Any?) {
        log(LogLevel.INFO, tag, message, *args)
    }

    fun w(tag: String, message: String, vararg args: Any?) {
        log(LogLevel.WARN, tag, message, *args)
    }

    fun e(tag: String, message: String, vararg args: Any?) {
        log(LogLevel.ERROR, tag, message, *args)
    }

    fun wtf(tag: String, message: String, vararg args: Any?) {
        log(LogLevel.ASSERT, tag, message, args)
    }

    private fun log(@LogLevel.LEVEL level: Int, tag: String, msg: String, vararg args: Any?) {
        if (logConfiguration.logLevel == LogLevel.NONE
            || logConfiguration.logLevel != LogLevel.ALL && logConfiguration.logLevel != level
        ) {
            return
        }
        val message: String = if (args.isEmpty()) msg else String.format(msg, *args)

        for (adapter in logConfiguration.logAdapters) {
            try {
                if (adapter.isLoggable(level, tag)) {
                    adapter.log(level, tag, message)
                }
            } catch (e: Exception) {
                //
            }
        }
    }
}