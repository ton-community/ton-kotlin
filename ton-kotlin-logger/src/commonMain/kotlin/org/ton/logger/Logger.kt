package org.ton.logger

import kotlin.jvm.JvmStatic

interface Logger {
    var level: Level

    fun log(level: Level, message: () -> String)
    fun trace(message: () -> String) = log(Level.TRACE, message)
    fun debug(message: () -> String) = log(Level.DEBUG, message)
    fun info(message: () -> String) = log(Level.INFO, message)
    fun warn(message: () -> String) = log(Level.WARN, message)
    fun fatal(message: () -> String) = log(Level.FATAL, message)

    enum class Level {
        TRACE,
        DEBUG,
        INFO,
        WARN,
        FATAL;
    }

    companion object {
        @JvmStatic
        fun println(name: String, level: Level = Level.INFO): Logger = PrintLnLogger(name, level)
    }
}
