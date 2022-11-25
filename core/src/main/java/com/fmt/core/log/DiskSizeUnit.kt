package com.fmt.core.log

enum class DiskSizeUnit {

    B {
        override fun toB(d: Long): Long {
            return d
        }
    },

    KB {
        override fun toB(d: Long): Long {
            return d * 1024
        }
    },

    MB {
        override fun toB(d: Long): Long {
            return d * 1024 * 1024
        }
    },

    GB {
        override fun toB(d: Long): Long {
            return d * 1024 * 1024 * 1024
        }
    };

    open fun toB(d: Long): Long {
        throw AbstractMethodError()
    }

}