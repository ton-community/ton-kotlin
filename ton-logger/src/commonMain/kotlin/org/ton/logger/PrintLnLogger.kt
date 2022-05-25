package org.ton.logger

class PrintLnLogger(
    private val name: String = "TON",
    override val level: Logger.Level = Logger.Level.INFO
) : Logger {
    override fun log(level: Logger.Level, message: () -> String) {
        if (level >= this.level) {
            println("[$name] [${level.name}] ${message()}")
        }
    }
}
