package com.fmt.core.log

import com.fmt.core.log.adapter.LogAdapter

/**
 * 日志初始化配置
 */
class LogConfiguration private constructor(builder: Builder) {

    internal var logLevel = LogLevel.ALL
    internal var logAdapters: MutableList<LogAdapter>

    init {
        logLevel = builder.logLevel
        logAdapters = builder.logAdapters
    }

    class Builder {
        internal var logLevel: Int = LogLevel.ALL
        internal val logAdapters: MutableList<LogAdapter> by lazy { mutableListOf() }

        fun logLevel(@LogLevel.LEVEL logLevel: Int): Builder {
            this.logLevel = logLevel
            return this
        }

        fun addAdapter(adapter: LogAdapter): Builder {
            if (!logAdapters.contains(adapter)) {
                logAdapters.add(adapter)
            }
            return this
        }

        fun build(): LogConfiguration {
            return LogConfiguration(this)
        }
    }
}