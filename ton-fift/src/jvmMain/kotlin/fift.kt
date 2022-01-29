import ton.fift.FiftException
import ton.fift.FiftInterpretator

fun main() {
    fift()
}

fun fift() {
    val fift = FiftInterpretator()
    fift.quiet {
        runFile("Fift.fif")
    }
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
    getResourceAsText(name).lines().forEachIndexed { index, line ->
        try {
            interpret(line)
        } catch (e: FiftException) {
            throw IllegalStateException("Exception occurred in $name:${index + 1}:$charPos: $currentLine", e)
        }
    }
}

fun getResourceAsText(path: String): String = object {}.javaClass.getResource(path)!!.readText()