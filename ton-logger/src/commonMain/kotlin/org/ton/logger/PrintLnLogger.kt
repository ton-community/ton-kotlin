package org.ton.logger

class PrintLnLogger(
    private val name: String = "TON",
    override var level: Logger.Level = Logger.Level.INFO
) : Logger {
    override fun log(level: Logger.Level, message: () -> String) {
        if (level >= this.level) {
            if (level == Logger.Level.DEBUG) {
                println("\u001B[37m[$name] [${level.name}] ${message()}\u001B[0m")
            } else {
                println("[$name] [${level.name}] ${message()}")
            }
        }
    }
}
