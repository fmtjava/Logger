package com.fmt.core.log.formatter

import com.fmt.core.log.LogLevel
import java.text.SimpleDateFormat
import java.util.*

internal class DefaultFormatter : Formatter {

    private val dateFormat: SimpleDateFormat by lazy {
        SimpleDateFormat("yyyy.MM.dd HH:mm:ss",
            Locale.CHINA)
    }

    override fun format(logLevel: Int, tag: String, log: String): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append(dateFormat.format(Date(System.currentTimeMillis())))
            .append('|')
            .append(LogLevel.getShortLevelName(logLevel))
            .append('|')
            .append(tag)
            .append("|:")
            .append(log)
            .append("\n")
        return stringBuilder.toString()
    }
}