package com.fmt.core.log.strategy

import android.util.Log

/**
 * Android Logcat 打印策略
 */
internal class LogcatLogStrategy : LogStrategy {

    override fun log(priority: Int, tag: String, message: String) {
        Log.println(priority, tag, message)
    }
}