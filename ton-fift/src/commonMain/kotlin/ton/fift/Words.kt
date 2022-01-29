package ton.fift

import ton.types.ExceptionCode
import ton.types.int257.Int257
import ton.types.int257.int257

fun FiftInterpretator.interpretDotSpace() {
    output("${stack.popInt257()} ")
}

fun FiftInterpretator.interpretDot() {
    output("${stack.popInt257()}")
}

fun FiftInterpretator.interpretBinaryDotSpace() {
    output("${stack.popInt257().toString(2)} ")
}

fun FiftInterpretator.interpretBinaryDot() {
    output(stack.popInt257().toString(2))
}

fun FiftInterpretator.interpretHexDotSpace() {
    output("${stack.popInt257().toString(16)} ")
}

fun FiftInterpretator.interpretHexDot() {
    output(stack.popInt257().toString(16))
}

fun FiftInterpretator.interpretUpperHexDotSpace() {
    output("${stack.popInt257().toString(16).uppercase()} ")
}

fun FiftInterpretator.interpretUpperHexDot() {
    output(stack.popInt257().toString(16).uppercase())
}

fun FiftInterpretator.interpretDotStack() {
    stack.forEach {
        output(it.toString())
        output(" ")
    }
    output("\n")
}

fun FiftInterpretator.interpretDrop() {
    stack.pop()
}

fun FiftInterpretator.interpret2Drop() {
    stack.pop()
    stack.pop()
}

fun FiftInterpretator.interpretDup() {
    stack.push(stack.get())
}

fun FiftInterpretator.interpret2Dup() {
    stack.push(stack[1])
    stack.push(stack[1])
}

fun FiftInterpretator.interpretOver() {
    stack.push(stack[1])
}

fun FiftInterpretator.interpret2Over() {
    stack.push(stack[3])
    stack.push(stack[3])
}

fun FiftInterpretator.interpretSwap() {
    stack.swap(0, 1)
}

fun FiftInterpretator.interpret2Swap() {
    stack.swap(0, 2)
    stack.swap(1, 3)
}

fun FiftInterpretator.interpretTuck() {
    stack.swap(0, 1)
    stack.push(stack[1])
}

fun FiftInterpretator.interpretNip() {
    stack.pop(1)
}

fun FiftInterpretator.interpretRot() {
    stack.swap(1, 2)
    stack.swap(0, 1)
}

fun FiftInterpretator.interpretRotRev() {
    stack.swap(0, 1)
    stack.swap(1, 2)
}

fun FiftInterpretator.interpretPick() {
    stack.push(stack[stack.popInt257().toInt()])
}

fun FiftInterpretator.interpretRoll() {
    val n = stack.popInt257().toInt()
    for (i in n downTo 1) {
        stack.swap(i, i - 1)
    }
}

fun FiftInterpretator.interpretRollRev() {
    val n = stack.popInt257().toInt()
    for (i in 0 until n) {
        stack.swap(i, i + 1)
    }
}

fun FiftInterpretator.interpretReverse() {
    val m = stack.popInt257().toInt()
    val n = stack.popInt257().toInt()
    val s = 2 * m + n - 1
    for (i in s - 1 shr 1 downTo m) {
        stack.swap(i, s - i)
    }
}

fun FiftInterpretator.interpretExch() {
    val n = stack.popInt257().toInt()
    stack.swap(0, n)
}

fun FiftInterpretator.interpretExch2() {
    val m = stack.popInt257().toInt()
    val n = stack.popInt257().toInt()
    stack.swap(m, n)
}

fun FiftInterpretator.interpretDepth() {
    stack.push(stack.depth)
}

fun FiftInterpretator.interpretConditionalDup() {
    val x = stack.popInt257()
    if (x.sign != 0) {
        stack.push(x)
    }
    stack.push(x)
}

fun FiftInterpretator.interpretPlus(y: Int257 = stack.popInt257()) {
    stack.push(stack.popInt257() + y)
}

fun FiftInterpretator.interpretMinus(y: Int257 = stack.popInt257()) {
    stack.push(stack.popInt257() - y)
}

fun FiftInterpretator.interpretNegate() {
    stack.push(-stack.popInt257())
}

fun FiftInterpretator.interpretTimes() {
    stack.push(stack.popInt257() * stack.popInt257())
}

fun FiftInterpretator.interpretDiv() {
    val y = stack.popInt257()
    stack.push(stack.popInt257() / y)
}

fun FiftInterpretator.interpretTimesDiv() {
    val z = stack.popInt257()
    val y = stack.popInt257()
    val x = stack.popInt257()
    stack.push(x * y / z)
}

fun FiftInterpretator.interpretMod() {
    val y = stack.popInt257()
    stack.push(stack.popInt257() mod y)
}

fun FiftInterpretator.interpretDivMod() {
    val y = stack.popInt257()
    val (div, mod) = stack.popInt257() divMod y
    stack.push(div)
    stack.push(mod)
}

fun FiftInterpretator.interpretShl(y: Int = stack.popInt257().toInt()) {
    stack.push(stack.popInt257() shl y)
}

fun FiftInterpretator.interpretShr(y: Int = stack.popInt257().toInt()) {
    stack.push(stack.popInt257() shr y)
}

fun FiftInterpretator.interpretAnd() {
    val y = stack.popInt257()
    stack.push(stack.popInt257() and y)
}

fun FiftInterpretator.interpretOr() {
    val y = stack.popInt257()
    stack.push(stack.popInt257() or y)
}

fun FiftInterpretator.interpretXor() {
    val y = stack.popInt257()
    stack.push(stack.popInt257() xor y)
}

fun FiftInterpretator.interpretNot() {
    stack.push(stack.popInt257().not())
}

fun FiftInterpretator.interpretLess() {
    val y = stack.popInt257()
    val x = stack.popInt257()
    stack.push(x < y)
}

fun FiftInterpretator.interpretGreater() {
    val y = stack.popInt257()
    val x = stack.popInt257()
    stack.push(x > y)
}

fun FiftInterpretator.interpretEqual() {
    val y = stack.popInt257()
    val x = stack.popInt257()
    stack.push(x == y)
}

fun FiftInterpretator.interpretNotEqual() {
    val y = stack.popInt257()
    val x = stack.popInt257()
    stack.push(x != y)
}

fun FiftInterpretator.interpretLessOrEqual() {
    val y = stack.popInt257()
    val x = stack.popInt257()
    stack.push(x <= y)
}

fun FiftInterpretator.interpretGreaterOrEqual() {
    val y = stack.popInt257()
    val x = stack.popInt257()
    stack.push(x >= y)
}

fun FiftInterpretator.interpretCmp() {
    val y = stack.popInt257()
    val x = stack.popInt257()
    stack.push(x.compareTo(y))
}

fun FiftInterpretator.interpretExecute() {
    val wordDef = stack.popWordDef()
    interpret(wordDef)
}

fun FiftInterpretator.interpretExecuteTimes() {
    val times = stack.popInt257().toInt()
    val wordDef = stack.popWordDef()
    when {
        times <= 0 -> return
        times == 1 -> interpret(wordDef)
        else -> repeat(times) {
            interpret(wordDef)
        }
    }
}

fun FiftInterpretator.interpretExecuteIf() {
    val wordDef = stack.popWordDef()
    val condition = stack.popInt257()
    if (!condition.isZero) {
        interpret(wordDef)
    }
}

fun FiftInterpretator.interpretExecuteIfNot() {
    val wordDef = stack.popWordDef()
    val condition = stack.popInt257()
    if (condition.isZero) {
        interpret(wordDef)
    }
}

fun FiftInterpretator.interpretCondition() {
    val falseWorldDef = stack.popWordDef()
    val trueWorldDef = stack.popWordDef()
    val condition = stack.popInt257()
    if (condition.isZero) {
        interpret(falseWorldDef)
    } else {
        interpret(trueWorldDef)
    }
}

fun FiftInterpretator.interpretUntil() {
    val wordDef = stack.popWordDef()
    while (true) {
        interpret(wordDef)
        if (!stack.popInt257().isZero) {
            break
        }
    }
}

fun FiftInterpretator.interpretWhile() {
    val body = stack.popWordDef()
    val wordDef = stack.popWordDef()
    while (true) {
        interpret(wordDef)
        if (stack.popInt257().isZero) {
            break
        }
        interpret(body)
    }
}

fun FiftInterpretator.interpretOpenBracket() {
    checkNotIntExec()
    interpretCompileOpenBracket()
    stack.pushArgCount(0)
    state++
}

fun FiftInterpretator.interpretCloseBracket() {
    checkCompile()
    interpretCompileCloseBracket()
    stack.pushArgCount(1)
    state--
}

fun FiftInterpretator.interpretCompileOpenBracket() {
    stack.push(WordList())
}

fun FiftInterpretator.interpretCompileCloseBracket() {
    val wordList = stack.popWordList()
    val wordDef = IterableWorldDef(wordList)
    stack.push(wordDef)
}

fun FiftInterpretator.interpretExecuteInternal() {
    val wordDef = stack.popWordDef()
    val count = stack.popInt257().toInt()
    if (stack.depth < count) {
        throw FiftException(ExceptionCode.StackUnderflow)
    }
    wordDef.execute(this)
}

fun FiftInterpretator.interpretCompileInternal() {
    val wordDef = stack.popWordDef()
    val count = stack.popInt257().toInt()
    doCompileLiterals(count)
    if (wordDef != NopWordDef) {
        doCompile(wordDef)
    }
}

fun FiftInterpretator.doCompile(wordDef: WordDef) {
    val wordList = stack.popWordList()
    if (wordDef != NopWordDef) {
        if (wordDef is WordList) {
            wordList.addAll(wordDef)
        } else {
            wordList.add(wordDef)
        }
    }
    stack.push(wordList)
}

fun FiftInterpretator.doCompileLiterals(count: Int) {
    check(count >= 0) { "cannot compile a negative number of literals" }
    val list = ArrayDeque<WordDef>()
    repeat(count) {
        val stackEntry = stack.pop()
        list.addFirst(PushStackWordDef(stackEntry))
    }
    val wordList = stack.popWordList()
    wordList.addAll(list)
    stack.push(wordList)
}

fun FiftInterpretator.interpretCreateInternal(mode: Int = stack.popInt257().toInt()) {
    val isActive = mode and 1 == 1
    val isPrefix = mode and 2 == 0
    try {
        val name = stack.popString().let {
            if (isPrefix) "$it " else it
        }
        val wordDef = stack.popWordDef()
        dictionary[name, isActive] = { wordDef.execute(this) }
    } catch (e: Exception) {
        interpretDotStack()
        e.printStackTrace()
    }
}

// { bl word <mode> 2 ' (create) } :: :
fun FiftInterpretator.interpretColon(mode: Int) {
    stack.push(scanWordTo())
    stack.push(mode)
    stack.push(2)
    stack.push(FunctionWordDef { interpretCreateInternal() })
}

fun FiftInterpretator.interpretTick() {
    skipSpace()
    val word = scanWordTo(' ', false)
    var wordDef = dictionary[word]
    if (wordDef == null) {
        wordDef = dictionary["$word "]
    }
    checkNotNull(wordDef) { "word `$word` undefined" }
    stack.push(wordDef)
    stack.pushArgCount(1)
}

fun FiftInterpretator.interpretWords() {
    dictionary.keys.forEach { word ->
        output(word)
        output(" ")
    }
    output("\n")
}

fun FiftInterpretator.interpretForgetInternal() {
    val word = stack.popString()
    checkNotNull(dictionary.remove(word)) { "Word `$word` not found" }
}

fun FiftInterpretator.interpretQuoteString() {
    stack.push(scanWordTo('\"', true))
    stack.pushArgCount(1)
}

fun FiftInterpretator.interpretChar() {
    val char = scanWordTo(' ').first().code
    stack.push(char)
    stack.pushArgCount(1)
}

fun FiftInterpretator.interpretEmit() {
    val char = stack.popInt257().toInt().toChar()
    output(char.toString())
}

fun FiftInterpretator.interpretSpace() {
    output(" ")
}

fun FiftInterpretator.interpretCr() {
    output("\n")
}

fun FiftInterpretator.interpretType() {
    val string = stack.popString()
    output(string)
}

fun FiftInterpretator.interpretStringConcat() {
    val y = stack.popString()
    stack.push(stack.popString() + y)
}

fun FiftInterpretator.interpretStringEqual() {
    val y = stack.popString()
    val x = stack.popString()
    stack.push(x == y)
}

fun FiftInterpretator.interpretStringCmp() {
    val y = stack.popString()
    val x = stack.popString()
    stack.push(x.compareTo(y))
}

fun FiftInterpretator.interpretWord() {
    val separator = stack.popInt257().toInt().toChar()
    val word = if (separator != ' ') {
        skipSpace()
        scanWordTo(separator, true)
    } else {
        skipSpace()
        scanWordTo(separator, false)
    }
    stack.push(word)
}

fun FiftInterpretator.interpretAbort() {
    val string = stack.popString()
    throw FiftException(ExceptionCode.AlternativeTermination, string)
}

fun FiftInterpretator.defineBasicWords() {
    dictionary[". "] = { interpretDotSpace() }
    dictionary["._ "] = { interpretDot() }
    dictionary["x. "] = { interpretHexDotSpace() }
    dictionary["x._ "] = { interpretHexDot() }
    dictionary["X. "] = { interpretUpperHexDotSpace() }
    dictionary["X._ "] = { interpretUpperHexDot() }
    dictionary["b. "] = { interpretBinaryDotSpace() }
    dictionary["b._ "] = { interpretBinaryDot() }
    dictionary[".s "] = { interpretDotStack() }
    // stack manipulation
    dictionary["drop "] = { interpretDrop() }
    dictionary["2drop "] = { interpret2Drop() }
    dictionary["dup "] = { interpretDup() }
    dictionary["2dup "] = { interpret2Dup() }
    dictionary["over "] = { interpretOver() }
    dictionary["2over "] = { interpret2Over() }
    dictionary["swap "] = { interpretSwap() }
    dictionary["2swap "] = { interpret2Swap() }
    dictionary["tuck "] = { interpretTuck() }
    dictionary["nip "] = { interpretNip() }
    dictionary["rot "] = { interpretRot() }
    dictionary["-rot "] = { interpretRotRev() }
    dictionary["pick "] = { interpretPick() }
    dictionary["roll "] = { interpretRoll() }
    dictionary["-roll "] = { interpretRollRev() }
    dictionary["reverse "] = { interpretReverse() }
    dictionary["exch "] = { interpretExch() }
    dictionary["exch2 "] = { interpretExch2() }
    dictionary["depth "] = { interpretDepth() }
    dictionary["?dup "] = { interpretConditionalDup() }

    // integer operations
    dictionary["false "] = { stack.push(0) }
    dictionary["true "] = { stack.push(-1) }
    dictionary["bl "] = { stack.push(32) }
    dictionary["+ "] = { interpretPlus() }
    dictionary["- "] = { interpretMinus() }
    dictionary["negate "] = { interpretNegate() }
    dictionary["1+ "] = { interpretPlus(int257(1)) }
    dictionary["1- "] = { interpretMinus(int257(1)) }
    dictionary["2+ "] = { interpretPlus(int257(2)) }
    dictionary["2- "] = { interpretMinus(int257(2)) }
    dictionary["2* "] = { interpretShl(1) }
    dictionary["2/ "] = { interpretShr(1) }
    dictionary["* "] = { interpretTimes() }
    dictionary["/ "] = { interpretDiv() }
    dictionary["*/ "] = { interpretTimesDiv() }
    dictionary["mod "] = { interpretMod() }
    dictionary["/mod "] = { interpretDivMod() }
    dictionary["<< "] = { interpretShl() }
    dictionary[">> "] = { interpretShr() }
    dictionary["and "] = { interpretAnd() }
    dictionary["or "] = { interpretOr() }
    dictionary["xor "] = { interpretXor() }
    dictionary["not "] = { interpretNot() }
    dictionary["< "] = { interpretLess() }
    dictionary["<= "] = { interpretLessOrEqual() }
    dictionary["> "] = { interpretGreater() }
    dictionary[">= "] = { interpretGreaterOrEqual() }
    dictionary["= "] = { interpretEqual() }
    dictionary["<> "] = { interpretNotEqual() }
    dictionary["cmp "] = { interpretCmp() }

    // execution control
    dictionary["execute "] = { interpretExecute() }
    dictionary["if "] = { interpretExecuteIf() }
    dictionary["ifnot "] = { interpretExecuteIfNot() }
    dictionary["cond "] = { interpretCondition() }
    dictionary["times "] = { interpretExecuteTimes() }
    dictionary["until "] = { interpretUntil() }
    dictionary["while "] = { interpretWhile() }

    // compile operations
    dictionary["{ ", true] = { interpretOpenBracket() }
    dictionary["} ", true] = { interpretCloseBracket() }
    dictionary["({) "] = { interpretCompileOpenBracket() }
    dictionary["(}) "] = { interpretCompileCloseBracket() }
    dictionary["(compile) "] = { interpretCompileInternal() }
    dictionary["(execute) "] = { interpretExecuteInternal() }
    dictionary["(create) "] = { interpretCreateInternal() }

    // dictionary operations
    dictionary["' ", true] = { interpretTick() }
    dictionary["’ ", true] = { interpretTick() }
    dictionary["nop "] = { /* nop */ }
    dictionary["'nop "] = { stack.push(NopWordDef) }
    dictionary["words "] = { interpretWords() }
    dictionary["(forget) "] = { interpretForgetInternal() }

    // string operations
    dictionary["\"", true] = { interpretQuoteString() }
    dictionary["char ", true] = { interpretChar() }
    dictionary["emit "] = { interpretEmit() }
    dictionary["space "] = { interpretSpace() }
    dictionary["cr "] = { interpretCr() }
    dictionary["type "] = { interpretType() }
    dictionary["$+ "] = { interpretStringConcat() }
    dictionary["$= "] = { interpretStringEqual() }
    dictionary["\$cmp "] = { interpretStringCmp() }

    // input parse
    dictionary["word "] = { interpretWord() }

    // exceptions
    dictionary["abort "] = { interpretAbort() }
}

fun FiftInterpretator.defineFiftWords() {
    dictionary[": ", true] = { interpretColon(0) }
    dictionary[":: ", true] = { interpretColon(1) }
    dictionary[":_ ", true] = { interpretColon(2) }
    dictionary["::_ ", true] = { interpretColon(3) }
}