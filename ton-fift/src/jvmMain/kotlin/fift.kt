import ton.fift.FiftInterpretator

fun main() {
    fift()
}

fun fift() {
    val vm = FiftInterpretator(output = {})
    getResourceAsText("Fift.fif").lines().forEach {
        vm.interpret(it)
    }
    vm.output = FiftInterpretator.OutputHandler { print(it) }

    while (true) {
        try {
            vm.interpret(readln())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun getResourceAsText(path: String): String = object {}.javaClass.getResource(path)!!.readText()