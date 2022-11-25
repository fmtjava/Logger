package com.fmt.core.log

import android.util.Log
import androidx.annotation.IntDef

/**
 * 日志打印类型
 */
class LogLevel {

    @IntDef(value = [VERBOSE, DEBUG, INFO, WARN, ERROR, ASSERT, ALL, NONE])
    @Retention(AnnotationRetention.SOURCE)
    annotation class LEVEL

    companion object {
        const val VERBOSE = Log.VERBOSE
        const val DEBUG = Log.DEBUG
        const val INFO = Log.INFO
        const val WARN = Log.WARN
        const val ERROR = Log.ERROR
        const val ASSERT = Log.ASSERT
        const val ALL = Int.MIN_VALUE
        const val NONE = Int.MAX_VALUE

        fun getShortLevelName(logLevel: Int): String = when (logLevel) {
            VERBOSE -> "V"
            DEBUG -> "D"
            INFO -> "I"
            WARN -> "W"
            ERROR -> "E"
            ASSERT -> "A"
            else -> if (logLevel < VERBOSE) {
                "V-" + (VERBOSE - logLevel)
            } else {
                "E+" + (logLevel - ERROR)
            }
        }
    }

}