package org.ton.logger

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

public class PrintLnLogger(
    private val name: () -> String = { "TON" },
    override var level: Logger.Level = Logger.Level.INFO
) : Logger {
    public constructor(name: String, level: Logger.Level = Logger.Level.INFO) : this({ name }, level)

    private val channel = Channel<() -> String>(Channel.UNLIMITED)
    private val job = GlobalScope.launch {
        while (true) {
            println(channel.receive().invoke())
        }
    }

    override fun log(level: Logger.Level, message: () -> String) {
        if (level >= this.level) {
            val messageLambda: () -> String = {
                when (level) {
                    Logger.Level.TRACE, Logger.Level.DEBUG -> {
                        "\u001B[37m[${name()}] [${level.name}] ${message()}\u001B[0m"
                    }

                    Logger.Level.WARN -> {
                        "\u001B[33m[${name()}] [${level.name}] ${message()}\u001B[0m"
                    }

                    Logger.Level.FATAL -> {
                        "\u001B[31m[${name()}] [${level.name}] ${message()}\u001B[0m"
                    }

                    else -> {
                        "[${name()}] [${level.name}] ${message()}"
                    }
                }
            }
            channel.trySend(messageLambda)
        }
    }
}
