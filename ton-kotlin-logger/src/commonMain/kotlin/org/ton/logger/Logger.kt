package org.ton.logger

import kotlin.jvm.JvmStatic

public interface Logger {
    public var level: Level

    public fun log(level: Level, message: () -> String)
    public fun trace(message: () -> String): Unit = log(Level.TRACE, message)
    public fun debug(message: () -> String): Unit = log(Level.DEBUG, message)
    public fun info(message: () -> String): Unit = log(Level.INFO, message)
    public fun warn(message: () -> String): Unit = log(Level.WARN, message)
    public fun fatal(message: () -> String): Unit = log(Level.FATAL, message)

    public enum class Level {
        TRACE,
        DEBUG,
        INFO,
        WARN,
        FATAL;
    }

    public companion object {
        @JvmStatic
        public fun println(name: String, level: Level = Level.INFO): Logger = PrintLnLogger(name, level)
    }
}
