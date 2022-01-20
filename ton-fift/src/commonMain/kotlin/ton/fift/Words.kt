package ton.fift

fun interpretDotSpace(fift: FiftInterpretator) = fift { output("${stack.popInt257()} ") }
fun interpretDot(fift: FiftInterpretator) = fift { output("${stack.popInt257()}") }
fun interpretBinaryDotSpace(fift: FiftInterpretator) = fift { output("${stack.popInt257().toString(2)} ") }
fun interpretBinaryDot(fift: FiftInterpretator) = fift { output(stack.popInt257().toString(2)) }
fun interpretHexDotSpace(fift: FiftInterpretator) = fift { output("${stack.popInt257().toString(16)} ") }
fun interpretHexDot(fift: FiftInterpretator) = fift { output(stack.popInt257().toString(16)) }
fun interpretUpperHexDotSpace(fift: FiftInterpretator) =
    fift { output("${stack.popInt257().toString(16).uppercase()} ") }

fun interpretUpperHexDot(fift: FiftInterpretator) = fift { output(stack.popInt257().toString(16).uppercase()) }
fun interpretDotStack(fift: FiftInterpretator) = fift {
    stack.storage.forEach {
        output(it.toString())
        output(" ")
    }
    output("\n")
}

fun interpretDrop(fift: FiftInterpretator) = fift { stack.pop() }

fun interpret2Drop(fift: FiftInterpretator) = fift {
    stack.pop()
    stack.pop()
}

fun interpretDup(fift: FiftInterpretator) = fift { stack.push(stack.get()) }
fun interpret2Dup(fift: FiftInterpretator) = fift {
    stack.push(stack[1])
    stack.push(stack[1])
}

fun interpretOver(fift: FiftInterpretator) = fift {
    stack.push(stack[1])
}

fun interpret2Over(fift: FiftInterpretator) = fift {
    stack.push(stack[3])
    stack.push(stack[3])
}

fun interpretSwap(fift: FiftInterpretator) = fift { stack.swap(0, 1) }

fun interpret2Swap(fift: FiftInterpretator) = fift {
    stack.swap(0, 2)
    stack.swap(1, 3)
}

fun interpretTuck(fift: FiftInterpretator) = fift {
    stack.swap(0, 1)
    stack.push(stack[1])
}

fun interpretNip(fift: FiftInterpretator) = fift { stack.pop(1) }

fun interpretRot(fift: FiftInterpretator) = fift {
    stack.swap(1, 2)
    stack.swap(0, 1)
}

fun interpretRotRev(fift: FiftInterpretator) = fift {
    stack.swap(0, 1)
    stack.swap(1, 2)
}

fun interpretPick(fift: FiftInterpretator) = fift { stack.push(stack[stack.popInt257().toInt()]) }

fun interpretRoll(fift: FiftInterpretator) = fift {
    val n = stack.popInt257().toInt()
    for (i in n downTo 1) {
        stack.swap(i, i - 1)
    }
}

fun interpretRollRev(fift: FiftInterpretator) = fift {
    val n = stack.popInt257().toInt()
    for (i in 0 until n) {
        stack.swap(i, i + 1)
    }
}

fun interpretReverse(fift: FiftInterpretator) = fift {
    val m = stack.popInt257().toInt()
    val n = stack.popInt257().toInt()
    val s = 2 * m + n - 1
    for (i in s - 1 shr 1 downTo m) {
        stack.swap(i, s - i)
    }
}

fun interpretExch(fift: FiftInterpretator) = fift {
    val n = stack.popInt257().toInt()
    stack.swap(0, n)
}

fun interpretExch2(fift: FiftInterpretator) = fift {
    val m = stack.popInt257().toInt()
    val n = stack.popInt257().toInt()
    stack.swap(m, n)
}

fun interpretDepth(fift: FiftInterpretator) = fift { stack.push(stack.storage.size) }

fun interpretConditionalDup(fift: FiftInterpretator) = fift {
    val x = stack.popInt257()
    if (x.sign != 0) {
        stack.push(x)
    }
    stack.push(x)
}

fun interpretPlus(fift: FiftInterpretator) = fift { stack.push(stack.popInt257() + stack.popInt257()) }
fun interpretMinus(fift: FiftInterpretator) = fift {
    val y = stack.popInt257()
    stack.push(stack.popInt257() - y)
}

fun interpretNegate(fift: FiftInterpretator) = fift { stack.push(-stack.popInt257()) }

fun interpretTimes(fift: FiftInterpretator) = fift { stack.push(stack.popInt257() * stack.popInt257()) }

fun interpretDiv(fift: FiftInterpretator) = fift {
    val y = stack.popInt257()
    stack.push(stack.popInt257() / y)
}

fun interpretMod(fift: FiftInterpretator) = fift {
    val y = stack.popInt257()
    stack.push(stack.popInt257() mod y)
}

fun interpretDivMod(fift: FiftInterpretator) = fift {
    val y = stack.popInt257()
    val (div, mod) = stack.popInt257() divMod y
    stack.push(div)
    stack.push(mod)
}

fun interpretShl(fift: FiftInterpretator) = fift {
    val y = stack.popInt257().toInt()
    stack.push(stack.popInt257() shl y)
}

fun interpretShr(fift: FiftInterpretator) = fift {
    val y = stack.popInt257().toInt()
    stack.push(stack.popInt257() shr y)
}

fun interpretAnd(fift: FiftInterpretator) = fift {
    val y = stack.popInt257()
    stack.push(stack.popInt257() and y)
}

fun interpretOr(fift: FiftInterpretator) = fift {
    val y = stack.popInt257()
    stack.push(stack.popInt257() or y)
}

fun interpretXor(fift: FiftInterpretator) = fift {
    val y = stack.popInt257()
    stack.push(stack.popInt257() xor y)
}

fun interpretNot(fift: FiftInterpretator) = fift { stack.push(stack.popInt257().not()) }

fun interpretQuoteString(fift: FiftInterpretator) = fift {
    stack.push(fift.input.scanWord("\""))
}

fun interpretEmit(fift: FiftInterpretator) = fift {
    val char = stack.popInt257().toInt().toChar()
    output(char.toString())
}

fun interpretStringConcat(fift: FiftInterpretator) = fift { stack.push(stack.popString() + stack.popString()) }

fun FiftInterpretator.defineBasicWords(dictionary: Dictionary) {
    dictionary[". "] = ::interpretDotSpace
    dictionary["._ "] = ::interpretDot
    dictionary["x. "] = ::interpretHexDotSpace
    dictionary["x._ "] = ::interpretHexDot
    dictionary["X. "] = ::interpretUpperHexDotSpace
    dictionary["X._ "] = ::interpretUpperHexDot
    dictionary["b. "] = ::interpretBinaryDotSpace
    dictionary["b._ "] = ::interpretBinaryDot
    dictionary[".s "] = ::interpretDotStack
    // stack manipulation
    dictionary["drop "] = ::interpretDrop
    dictionary["2drop "] = ::interpret2Drop
    dictionary["dup "] = ::interpretDup
    dictionary["2dup "] = ::interpret2Dup
    dictionary["over "] = ::interpretOver
    dictionary["2over "] = ::interpretOver
    dictionary["swap "] = ::interpretSwap
    dictionary["2swap "] = ::interpret2Swap
    dictionary["tuck "] = ::interpretTuck
    dictionary["nip "] = ::interpretNip
    dictionary["rot "] = ::interpretRot
    dictionary["-rot "] = ::interpretRotRev
    dictionary["pick "] = ::interpretPick
    dictionary["roll "] = ::interpretRoll
    dictionary["-roll "] = ::interpretRollRev
    dictionary["reverse "] = ::interpretReverse
    dictionary["exch "] = ::interpretExch
    dictionary["exch2 "] = ::interpretExch2
    dictionary["depth "] = ::interpretDepth
    dictionary["?dup "] = ::interpretConditionalDup
    // integer operations
    dictionary["+ "] = ::interpretPlus
    dictionary["- "] = ::interpretMinus
    dictionary["negate "] = ::interpretNegate
    dictionary["* "] = ::interpretTimes
    dictionary["/ "] = ::interpretDiv
    dictionary["mod "] = ::interpretMod
    dictionary["/mod "] = ::interpretDivMod
    dictionary["<< "] = ::interpretShl
    dictionary[">> "] = ::interpretShr
    dictionary["and "] = ::interpretAnd
    dictionary["or "] = ::interpretOr
    dictionary["xor "] = ::interpretXor
    dictionary["not "] = ::interpretNot
    // compile operations

    // string operations
    dictionary["\"", true] = ::interpretQuoteString
    dictionary["emit "] = ::interpretEmit
    dictionary["space "] = { it.output(" ") }
    dictionary["cr "] = { it.output("\n") }
    dictionary["$+ "] = ::interpretStringConcat
}