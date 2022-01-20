package ton.fift

import io.ktor.utils.io.core.*
import ton.fift.FiftInterpretator.OutputHandler

class FiftInterpretator(
    var input: Input,
    val stack: Stack = Stack(),
    val dictionary: Dictionary = Dictionary(),
    val output: OutputHandler = OutputHandler { print(it) },
) {
    init {
        defineBasicWords(dictionary)
    }

    fun execute() {
        var executed = false
        while (input.remaining > 0) {
            try {
                val word = input.scanWord()
                println("scan word: `$word`")
                var wordDef = dictionary[word]
                if (wordDef == null) {
                    wordDef = dictionary["$word "]
                    input.discard(1)
                }
                if (wordDef != null) {
                    wordDef.execute(this)
                } else {
                    stack.push(int257(word))
                }
                executed = true
            } catch (e: FiftException) {
                output(e.toString())
            }
        }
        if (executed) {
            output(" ok\n")
        }
    }

    operator fun invoke(block: FiftInterpretator.() -> Unit) = apply(block)

    fun interface OutputHandler {
        fun output(string: String)

        operator fun invoke(string: String) = output(string)
    }
}

fun Input.scanWord(delimiters: String = " \n\t"): String {
    skipSpace()
    val word = buildString {
        readUTF8UntilDelimiterTo(this, delimiters)
    }
    return word
}

fun Input.skipSpace() {
    while (remaining > 0) {
        val char = peekCharUtf8()
        if (char == ' ' || char == '\t' || char == '\n') {
            discardExact(1)
        } else {
            break
        }
    }
}