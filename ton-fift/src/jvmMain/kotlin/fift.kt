import ton.fift.FiftInterpretator

fun main() {
    fift()
}

fun fift() {
    val fift = FiftInterpretator()
    fift.quiet {
        runFile("Fift.fif")
    }
    fift.runFile("Tests.fif")
    fift.output = { print(it) }
    while (true) {
        try {
            fift.interpret(readln())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun FiftInterpretator.quiet(block: FiftInterpretator.() -> Unit) {
    val o = output
    output = {}
    block()
    output = o
}

fun FiftInterpretator.runFile(name: String) {
    getResourceAsText(name).lines().forEach {
        interpret(it)
    }
}

fun getResourceAsText(path: String): String = object {}.javaClass.getResource(path)!!.readText()