package org.ton.logger

class PrintLnLogger(
    private val name: () -> String = { "TON" },
    override var level: Logger.Level = Logger.Level.INFO
) : Logger {
    constructor(name: String, level: Logger.Level = Logger.Level.INFO) : this({ name }, level)

    override fun log(level: Logger.Level, message: () -> String) {
        if (level >= this.level) {
            when (level) {
                Logger.Level.DEBUG -> {
                    println("\u001B[37m[${name()}] [${level.name}] ${message()}\u001B[0m")
                }

                Logger.Level.WARN -> {
                    println("\u001B[33m[${name()}] [${level.name}] ${message()}\u001B[0m")
                }

                Logger.Level.FATAL -> {
                    println("\u001B[31m[${name()}] [${level.name}] ${message()}\u001B[0m")
                }

                else -> {
                    println("[${name()}] [${level.name}] ${message()}")
                }
            }
        }
    }
}
