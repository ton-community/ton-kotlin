import ton.fift.FiftInterpretator

fun main() {
    fift()
}

fun fift() {
    val fift = FiftInterpretator(output = {})
    getResourceAsText("Fift.fif").lines().forEach {
        fift.interpret(it)
    }
    fift.output = { print(it) }
    while (true) {
        try {
            fift.interpret(readln())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun getResourceAsText(path: String): String = object {}.javaClass.getResource(path)!!.readText()