package com.fmt.core.log.formatter

interface Formatter {
    fun format(logLevel: Int, tag: String, log: String): String
}