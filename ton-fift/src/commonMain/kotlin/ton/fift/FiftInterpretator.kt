package ton.fift

class FiftInterpretator(
    val stack: Stack = Stack(),
    val dictionary: Dictionary = Dictionary(),
    var output: (String) -> Unit = { print(it) },
) {
    init {
        defineBasicWords()
    }

    var state: Int = 0
    private var charPos = 0
    private var currentLine: String = ""

    fun interpret(line: String) = try {
        currentLine = line
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
                interpret(wordDef)
            } else {
                charPos += word.length
                stack.push(int257(word))
                stack.pushArgCount(1)
                compileExecute()
            }
        }
        output(" ok\n")
    } catch (e: FiftException) {
        output("${e.message.toString()} ")
        e.printStackTrace()
    }

    fun interpret(wordDef: WordDef) {
        if (wordDef.isActive) {
            wordDef.execute(this)
            compileExecute()
        } else {
            stack.push(0)
            stack.push(wordDef)
            compileExecute()
        }
    }

    fun compileExecute() {
        if (state > 0) {
            interpretCompileInternal()
        } else {
            interpretExecuteInternal()
        }
    }

    fun canRead() = charPos in 0..currentLine.lastIndex

    fun scanWordTo(separator: Char, readSeparator: Boolean = false): String {
        val wordBuilder = StringBuilder()
        while (canRead()) {
            val char = currentLine[charPos]
            if (char != separator) {
                charPos++
                wordBuilder.append(char)
            } else {
                if (readSeparator) {
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
}

fun FiftInterpretator.checkCompile() = check(state > 0) { "Compilation mode only" }
fun FiftInterpretator.checkExecute() = check(state > 0) { "Interpretation mode only" }
fun FiftInterpretator.checkNotIntExec() = check(state >= 0) { "not allowed in internal interpret mode" }
fun FiftInterpretator.checkIntExec() = check(state < 0) { "internal interpret mode only" }
