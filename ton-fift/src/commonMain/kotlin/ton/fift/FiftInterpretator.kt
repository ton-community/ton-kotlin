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

    private var charPos = 0
    private var currentLine: String = ""

    fun execute() {
        while (input.remaining > 0) {
            try {
                currentLine = input.readUTF8Line() ?: break
                charPos = 0

                while (canRead()) {
                    skipSpace()
                    val pos = charPos
                    var word = scanWordTo(' ')
                    var wordDef = dictionary[word]

                    if (wordDef == null) {
                        wordDef = dictionary["$word "]
                        if (wordDef != null) {
                            charPos++
                        }
                    }

                    if (wordDef == null) {
                        charPos = pos
                        val wordBuilder = StringBuilder()
                        while (canRead()) {
                            val char = currentLine[charPos++]
                            if (char != ' ' && char != '\t') {
                                wordBuilder.append(char)

                                val currentWord = wordBuilder.toString()
                                wordDef = dictionary[currentWord]
                                if (wordDef != null) {
                                    word = currentWord
                                    break
                                }
                            }
                        }
                        if (wordDef == null) {
                            charPos = pos
                        }
                    }

                    if (wordDef != null) {
                        wordDef.execute(this)
                    } else {
                        stack.push(int257(word))
                        charPos += word.length
                    }
                }
                output(" ok\n")
            } catch (e: FiftException) {
                output(e.message.toString())
            }
        }
    }

    fun canRead() = charPos in 0..currentLine.lastIndex

    fun scanWordTo(delimiter: Char, readDelimiter: Boolean = false): String {
        val wordBuilder = StringBuilder()
        while (canRead()) {
            val char = currentLine[charPos]
            if (char != delimiter) {
                charPos++
                wordBuilder.append(char)
            } else {
                if (readDelimiter) {
                    charPos++
                }
                break
            }
        }
        return wordBuilder.toString()
    }

    fun skipSpace() {
        while (canRead()) {
            val char = currentLine[charPos]
            if (char == ' ' || char == '\t' || char == '\n') {
                charPos++
            } else {
                break
            }
        }
    }

    operator fun invoke(block: FiftInterpretator.() -> Unit) = apply(block)

    fun interface OutputHandler {
        fun output(string: String)

        operator fun invoke(string: String) = output(string)
    }
}

