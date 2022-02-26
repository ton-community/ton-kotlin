package ton

import ton.fift.FiftInterpretator

fun ton(args: Array<String>) {
    if (args.isNotEmpty()) {
        when (args[0]) {
            "fift" -> fift(args.copyOfRange(1, args.lastIndex))
            "help" -> help()
        }
    } else {
        help()
    }
}

fun help() {
    println("help - list of available arguments")
    println("fift - run Fift interpretator")
}

fun fift(args: Array<String>) {
    val fift = FiftInterpretator()
    while (true) {
        try {
            fift.interpret(readln())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}