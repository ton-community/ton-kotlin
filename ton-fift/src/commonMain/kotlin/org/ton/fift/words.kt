package org.ton.fift

import io.ktor.util.*
import io.ktor.utils.io.core.*
import org.ton.bigint.*
import org.ton.cell.CellBuilder

fun Dictionary.defineBasicWords() {
    this[". "] = { interpretDotSpace() }
    this["._ "] = { interpretDot() }
    this["x. "] = { interpretHexDotSpace() }
    this["x._ "] = { interpretHexDot() }
    this["X. "] = { interpretUpperHexDotSpace() }
    this["X._ "] = { interpretUpperHexDot() }
    this["b. "] = { interpretBinaryDotSpace() }
    this["b._ "] = { interpretBinaryDot() }
    this[".s "] = { interpretDotStack() }
    // stack manipulation
    this["drop "] = { interpretDrop() }
    this["2drop "] = { interpret2Drop() }
    this["dup "] = { interpretDup() }
    this["2dup "] = { interpret2Dup() }
    this["over "] = { interpretOver() }
    this["2over "] = { interpret2Over() }
    this["swap "] = { interpretSwap() }
    this["2swap "] = { interpret2Swap() }
    this["tuck "] = { interpretTuck() }
    this["nip "] = { interpretNip() }
    this["rot "] = { interpretRot() }
    this["-rot "] = { interpretRotRev() }
    this["pick "] = { interpretPick() }
    this["roll "] = { interpretRoll() }
    this["-roll "] = { interpretRollRev() }
    this["reverse "] = { interpretReverse() }
    this["exch "] = { interpretExch() }
    this["exch2 "] = { interpretExch2() }
    this["depth "] = { interpretDepth() }
    this["?dup "] = { interpretConditionalDup() }

    // integer operations
    this["false "] = { stack.push(0) }
    this["true "] = { stack.push(-1) }
    this["bl "] = { stack.push(32) }
    this["+ "] = { interpretPlus() }
    this["- "] = { interpretMinus() }
    this["negate "] = { interpretNegate() }
    this["1+ "] = { interpretPlus(BigInt(1)) }
    this["1- "] = { interpretMinus(BigInt(1)) }
    this["2+ "] = { interpretPlus(BigInt(2)) }
    this["2- "] = { interpretMinus(BigInt(2)) }
    this["2* "] = { interpretShl(1) }
    this["2/ "] = { interpretShr(1) }
    this["* "] = { interpretTimes() }
    this["/ "] = { interpretDiv() }
    this["*/ "] = { interpretTimesDiv() }
    this["mod "] = { interpretMod() }
    this["/mod "] = { interpretDivMod() }
    this["<< "] = { interpretShl() }
    this[">> "] = { interpretShr() }
    this["and "] = { interpretAnd() }
    this["or "] = { interpretOr() }
    this["xor "] = { interpretXor() }
    this["not "] = { interpretNot() }
    this["< "] = { interpretLess() }
    this["<= "] = { interpretLessOrEqual() }
    this["> "] = { interpretGreater() }
    this[">= "] = { interpretGreaterOrEqual() }
    this["= "] = { interpretEqual() }
    this["<> "] = { interpretNotEqual() }
    this["cmp "] = { interpretCmp() }
    this["0= "] = { interpretEqualZero() }
    this["0<> "] = { interpretNotEqualZero() }
    this["0< "] = { interpretLessZero() }
    this["0<= "] = { interpretLessOrEqualZero() }
    this["0> "] = { interpretGreaterZero() }
    this["0>= "] = { interpretGreaterOrEqualZero() }

    // execution control
    this["execute "] = { interpretExecute() }
    this["if "] = { interpretExecuteIf() }
    this["ifnot "] = { interpretExecuteIfNot() }
    this["cond "] = { interpretCondition() }
    this["times "] = { interpretExecuteTimes() }
    this["until "] = { interpretUntil() }
    this["while "] = { interpretWhile() }

    // compile operations
    this["{ ", true] = { interpretOpenBracket() }
    this["} ", true] = { interpretCloseBracket() }
    this["({) "] = { interpretCompileOpenBracket() }
    this["(}) "] = { interpretCompileCloseBracket() }
    this["(compile) "] = { interpretCompileInternal() }
    this["(execute) "] = { interpretExecuteInternal() }
    this["(create) "] = { interpretCreateInternal() }

    // dictionary operations
    this["' ", true] = { interpretTick() }
    this["nop "] = { /* nop */ }
    this["'nop "] = { stack.push(NopWordDef) }
    this["find "] = { interpretFind() }
    this["words "] = { interpretWords() }
    this["(forget) "] = { interpretForgetInternal() }

    // string operations
    this["\"", true] = { interpretQuoteString() }
    this["type "] = { interpretType() }
    this["cr "] = { interpretEmitConst('\n') }
    this["emit "] = { interpretEmit() }
    this["char ", true] = { interpretChar() }
    this["(char) "] = { interpretCharInternal() }
    this["bl "] = WordDef(BigInt(32))
    this["space "] = { interpretEmitConst(' ') }
    this["$+ "] = { interpretStringConcat() }
    this["$= "] = { interpretStringEqual() }
    this["string? "] = { interpretIsString() }
    this["chr "] = { interpretChr() }
    this["hold "] = { interpretHold() }
    this["(number) "] = { interpretNumberInternal() }
    this["(-trailing) "] = { interpretStringTrailing() }
    this["-trailing "] = { interpretStringTrailing(" ") }
    this["-trailing0 "] = { interpretStringTrailing("0") }
    this["\$cmp "] = { interpretStringCmp() }
    this["\$len "] = { interpretStringLength() }
    this["\$reverse"] = { interpretStringReverse() }
    this["\$pos"] = { interpretStringPos() }

    // bytes
    this["x>B "] = { interpretHexToBytes() }

    // input parse
    this["word "] = { interpretWord() }

    // exceptions
    this["abort "] = { interpretAbort() }

    // box
    this["hole "] = { interpretHole() }
    this["box "] = { interpretBox() }
    this["@ "] = { interpretBoxFetch() }
    this["! "] = { interpretBoxStore() }
    this["null "] = { interpretNull() }
    this["null? "] = { interpretIsNull() }

    // cell manipulation
    this["<b "] = { interpretCellBuilder() }
    this["b> "] = { interpretCellBuild() }
    this["s, "] = { interpretAppendCellSlice() }
    this["hashB "] = { interpretHashB() }
}

fun Dictionary.defineFiftWords() {
    this[": ", true] = { interpretColon(0) }
    this[":: ", true] = { interpretColon(1) }
    this[":_ ", true] = { interpretColon(2) }
    this["::_ ", true] = { interpretColon(3) }
}

fun FiftInterpretator.interpretDotSpace() {
    output("${stack.popInt()} ")
}

fun FiftInterpretator.interpretDot() {
    output("${stack.popInt()}")
}

fun FiftInterpretator.interpretBinaryDotSpace() {
    output("${stack.popInt().toString(2)} ")
}

fun FiftInterpretator.interpretBinaryDot() {
    output(stack.popInt().toString(2))
}

fun FiftInterpretator.interpretHexDotSpace() {
    output("${stack.popInt().toString(16)} ")
}

fun FiftInterpretator.interpretHexDot() {
    output(stack.popInt().toString(16))
}

fun FiftInterpretator.interpretUpperHexDotSpace() {
    output("${stack.popInt().toString(16).uppercase()} ")
}

fun FiftInterpretator.interpretUpperHexDot() {
    output(stack.popInt().toString(16).uppercase())
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
    stack.push(stack[stack.popInt().toInt()])
}

fun FiftInterpretator.interpretRoll() {
    val n = stack.popInt().toInt()
    for (i in n downTo 1) {
        stack.swap(i, i - 1)
    }
}

fun FiftInterpretator.interpretRollRev() {
    val n = stack.popInt().toInt()
    for (i in 0 until n) {
        stack.swap(i, i + 1)
    }
}

fun FiftInterpretator.interpretReverse() {
    val m = stack.popInt().toInt()
    val n = stack.popInt().toInt()
    val s = 2 * m + n - 1
    for (i in s - 1 shr 1 downTo m) {
        stack.swap(i, s - i)
    }
}

fun FiftInterpretator.interpretExch() {
    val n = stack.popInt().toInt()
    stack.swap(0, n)
}

fun FiftInterpretator.interpretExch2() {
    val m = stack.popInt().toInt()
    val n = stack.popInt().toInt()
    stack.swap(m, n)
}

fun FiftInterpretator.interpretDepth() {
    stack.push(stack.depth)
}

fun FiftInterpretator.interpretConditionalDup() {
    val x = stack.popInt()
    if (x.sign != 0) {
        stack.push(x)
    }
    stack.push(x)
}

fun FiftInterpretator.interpretPlus(y: BigInt = stack.popInt()) {
    stack.push(stack.popInt() + y)
}

fun FiftInterpretator.interpretMinus(y: BigInt = stack.popInt()) {
    stack.push(stack.popInt() - y)
}

fun FiftInterpretator.interpretNegate() {
    stack.push(-stack.popInt())
}

// 0001
// 1000
fun FiftInterpretator.interpretTimes() {
    stack.push(stack.popInt() * stack.popInt())
}

fun FiftInterpretator.interpretDiv() {
    val y = stack.popInt()
    stack.push(stack.popInt() / y)
}

fun FiftInterpretator.interpretTimesDiv() {
    val z = stack.popInt()
    val y = stack.popInt()
    val x = stack.popInt()
    stack.push(x * y / z)
}

fun FiftInterpretator.interpretMod() {
    val y = stack.popInt()
    stack.push(stack.popInt() mod y)
}

fun FiftInterpretator.interpretDivMod() {
    val y = stack.popInt()
    val (div, mod) = stack.popInt() divRem y
    stack.push(div)
    stack.push(mod)
}

fun FiftInterpretator.interpretShl(y: Int = stack.popInt().toInt()) {
    stack.push(stack.popInt() shl y)
}

fun FiftInterpretator.interpretShr(y: Int = stack.popInt().toInt()) {
    stack.push(stack.popInt() shr y)
}

fun FiftInterpretator.interpretAnd() {
    val y = stack.popInt()
    stack.push(stack.popInt() and y)
}

fun FiftInterpretator.interpretOr() {
    val y = stack.popInt()
    stack.push(stack.popInt() or y)
}

fun FiftInterpretator.interpretXor() {
    val y = stack.popInt()
    stack.push(stack.popInt() xor y)
}

fun FiftInterpretator.interpretNot() {
    stack.push(stack.popInt().not())
}

fun FiftInterpretator.interpretLess() {
    val y = stack.popInt()
    val x = stack.popInt()
    stack.push(x < y)
}

fun FiftInterpretator.interpretGreater() {
    val y = stack.popInt()
    val x = stack.popInt()
    stack.push(x > y)
}

fun FiftInterpretator.interpretEqual() {
    val y = stack.popInt()
    val x = stack.popInt()
    stack.push(x == y)
}

fun FiftInterpretator.interpretNotEqual() {
    val y = stack.popInt()
    val x = stack.popInt()
    stack.push(x != y)
}

fun FiftInterpretator.interpretEqualZero() {
    val x = stack.popInt()
    stack.push(x.equals(0))
}

fun FiftInterpretator.interpretNotEqualZero() {
    val x = stack.popInt()
    stack.push(!x.equals(0))
}

fun FiftInterpretator.interpretLessOrEqualZero() {
    val x = stack.popInt()
    stack.push(x.sign == -1 || x.equals(0))
}

fun FiftInterpretator.interpretLessZero() {
    val x = stack.popInt()
    stack.push(x.sign == -1)
}

fun FiftInterpretator.interpretGreaterOrEqualZero() {
    val x = stack.popInt()
    stack.push(x.sign == 1 || x.equals(0))
}

fun FiftInterpretator.interpretGreaterZero() {
    val x = stack.popInt()
    stack.push(x.sign == 1)
}

fun FiftInterpretator.interpretLessOrEqual() {
    val y = stack.popInt()
    val x = stack.popInt()
    stack.push(x <= y)
}

fun FiftInterpretator.interpretGreaterOrEqual() {
    val y = stack.popInt()
    val x = stack.popInt()
    stack.push(x >= y)
}

fun FiftInterpretator.interpretCmp() {
    val y = stack.popInt()
    val x = stack.popInt()
    stack.push(x.compareTo(y))
}

fun FiftInterpretator.interpretExecute() {
    val wordDef = stack.popWordDef()
    interpret(wordDef)
}

fun FiftInterpretator.interpretExecuteTimes() {
    val times = stack.popInt().toInt()
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
    val condition = stack.popInt()
    if (!condition.isZero) {
        interpret(wordDef)
    }
}

fun FiftInterpretator.interpretExecuteIfNot() {
    val wordDef = stack.popWordDef()
    val condition = stack.popInt()
    if (condition.isZero) {
        interpret(wordDef)
    }
}

fun FiftInterpretator.interpretCondition() {
    val falseWorldDef = stack.popWordDef()
    val trueWorldDef = stack.popWordDef()
    val condition = stack.popInt()
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
        if (!stack.popInt().isZero) {
            break
        }
    }
}

fun FiftInterpretator.interpretWhile() {
    val body = stack.popWordDef()
    val wordDef = stack.popWordDef()
    while (true) {
        interpret(wordDef)
        if (stack.popInt().isZero) {
            break
        }
        interpret(body)
    }
}

fun FiftInterpretator.interpretOpenBracket() {
    checkNotIntExec()
    interpretCompileOpenBracket()
    stack.pushArgCount(0)
    logger.debug { "New state: $state -> ${state + 1}" }
    state++
}

fun FiftInterpretator.interpretCloseBracket() {
    checkCompile()
    interpretCompileCloseBracket()
    stack.pushArgCount(1)
    logger.debug { "New state: $state -> ${state - 1}" }
    state--
}

fun FiftInterpretator.interpretCompileOpenBracket() {
    stack.push(WordList())
}

fun FiftInterpretator.interpretCompileCloseBracket() {
    val wordList = stack.popWordList()
    val wordDef = SequentialWordDef(wordList)
    stack.push(wordDef)
}

fun FiftInterpretator.interpretExecuteInternal() {
    val wordDef = stack.popWordDef()
    val count = stack.popInt().toInt()
    if (stack.depth < count) {
        // TODO: exception code
//        throw FiftException(ExceptionCode.StackUnderflow)
        throw FiftException(-1, "Stack underflow")
    }
    wordDef.execute(this)
}

fun FiftInterpretator.interpretCompileInternal() {
    val wordDef = stack.popWordDef()
    val count = stack.popInt().toInt()
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
        try {
            val stackEntry = stack.pop()
            list.addFirst(WordDef(stackEntry))
        } catch (e: FiftStackOverflow) {
            throw FiftStackOverflow("expected $count elements: $list")
        }
    }
    val wordList = stack.popWordList()
    wordList.addAll(list)
    stack.push(wordList)
}

fun FiftInterpretator.interpretCreateInternal() {
    val mode = stack.popInt().toInt()
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
    stack.push(scanInput())
    stack.push(mode)
    stack.push(2)
    stack.push(interpretCreateInternal())
}

fun FiftInterpretator.interpretTick() {
    skipSpace()
    val word = scanInput(' ', false)
    var wordDef = dictionary[word]
    if (wordDef == null) {
        wordDef = dictionary["$word "]
    }
    checkNotNull(wordDef) { "word `$word` undefined" }
    stack.push(wordDef)
    stack.pushArgCount(1)
}

fun FiftInterpretator.interpretFind() {
    val word = stack.popString()
    val wordDef = dictionary[word]
    if (wordDef != null) {
        stack.push(wordDef)
        if (wordDef.isActive) {
            stack.push(1)
        } else {
            stack.push(-1)
        }
    } else {
        stack.push(0)
    }
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
    stack.push(scanInput('\"', true))
    stack.pushArgCount(1)
}

fun FiftInterpretator.interpretChar() {
    val char = scanInput()
    val code = char.utf8Code
    check(code >= 0) { "Exactly one character expected" }
    stack.push(code)
    stack.pushArgCount(1)
}

fun FiftInterpretator.interpretCharInternal() {
    val char = stack.popString()
    val code = char.utf8Code
    check(code >= 0) { "Exactly one character expected" }
    stack.push(code)
}

fun FiftInterpretator.interpretEmit() {
    val char = stack.popInt().toInt().utf8Char
    output(char)
}

fun FiftInterpretator.interpretEmitConst(char: Char) {
    output(char.toString())
}

fun FiftInterpretator.interpretType() {
    val string = stack.popString()
    output(string)
}

fun FiftInterpretator.interpretIsString() {
    val isString = stack.pop() is String
    stack.push(isString)
}

/**
 * `x – S`
 *
 * returns a new String `S` consisting of one UTF-8 encoded character with Unicode codepoint `x`.
 */
fun FiftInterpretator.interpretChr() {
    val char = stack.popInt().toInt().utf8Char
    stack.push(char)
}

/**
 * `S x – S`
 *
 * appends to String `S` one UTF-8 encoded character with Unicode codepoint `x`. Equivalent to `chr $+`.
 */
fun FiftInterpretator.interpretHold() {
    val char = stack.popInt().toInt().utf8Char
    val string = stack.popString()
    stack.push(string + char)
}

/**
 * `(number)`
 *
 * `S – 0` or `x 1` or `x y 2`
 *
 * attempts to parse the String `S` as an integer or fractional literal.
 * On failure, returns a single `0`.
 * On success, returns `x 1` if `S` is a valid integer literal with value `x`, or `x y 2`
 * if `S` is a valid fractional literal with value `x`/`y`.
 */
fun FiftInterpretator.interpretNumberInternal() {
    val string = stack.popString()
    val int257s = string.parseInt256()
    when (int257s.size) {
        1 -> {
            stack.push(int257s.first())
            stack.push(1)
        }
        2 -> {
            stack.push(int257s[0])
            stack.push(int257s[1])
            stack.push(2)
        }
        else -> stack.push(0)
    }
}

fun FiftInterpretator.interpretStringTrailing(trailing: String = stack.popInt().toInt().utf8Char) {
    val string = stack.popString()
    stack.push(string.removePrefix(trailing))
}

/**
 * `$+`
 *
 * `S S' - S.S'`
 *
 * Concatenates two strings.
 */
fun FiftInterpretator.interpretStringConcat() {
    val y = stack.popString()
    stack.push(stack.popString() + y)
}

fun FiftInterpretator.interpretStringLength() {
    val string = stack.popString()
    val length = string.toByteArray().size
    stack.push(length)
}

fun FiftInterpretator.interpretStringReverse() {
    val string = stack.popString()
    stack.push(string.reversed())
}

fun FiftInterpretator.interpretStringPos() {
    val s2 = stack.popString()
    val s1 = stack.popString()
    val index = s1.indexOf(s2)
    stack.push(index)
}

fun FiftInterpretator.interpretHexToBytes() {
    val string = stack.popString()
    val bytes = hex(string)
    stack.push(bytes)
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
    val separator = stack.popInt().toInt().toChar()
    val word = if (separator != ' ') {
        skipSpace()
        scanInput(separator, true)
    } else {
        skipSpace()
        scanInput(separator, false)
    }
    stack.push(word)
}

fun FiftInterpretator.interpretAbort() {
    val string = stack.popString()
    // TODO: exception code
//    throw FiftException(ExceptionCode.AlternativeTermination, string)
    throw FiftException(-1, string)
}

fun FiftInterpretator.interpretHole() {
    stack.push(Box())
}

fun FiftInterpretator.interpretBox() {
    val value = stack.pop()
    val box = Box(value)
    stack.push(box)
}

fun FiftInterpretator.interpretBoxFetch() {
    val box = stack.popBox()
    val value = box.value
    stack.push(value)
}

fun FiftInterpretator.interpretBoxStore() {
    val box = stack.popBox()
    box.value = stack.pop()
}

fun FiftInterpretator.interpretNull() {
    stack.push(Unit)
}

fun FiftInterpretator.interpretIsNull() {
    val value = stack.pop()
    val isNull = value == Unit
    stack.push(isNull)
}

fun FiftInterpretator.interpretCellBuilder() {
    stack.push(CellBuilder())
}

fun FiftInterpretator.interpretCellBuild() {
    val builder = stack.popCellBuilder()
    val cell = builder.endCell()
    stack.push(cell)
}

fun FiftInterpretator.interpretAppendCellSlice() {
    val builder = stack.popCellBuilder()
    val slice = stack.popCellSlice()
    builder.storeSlice(slice)
    stack.push(builder)
}

fun FiftInterpretator.interpretHashB() {
    val cell = stack.popCell()
    val hash = cell.hash()
    stack.push(hash)
}