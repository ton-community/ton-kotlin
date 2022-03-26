package ton.tlb

import ton.bitstring.BitString
import ton.bitstring.toInt

class BitPrefix(
    prefix: Sequence<BitString>
) : Iterable<BitString> {
    val prefix = prefix.sorted().toSet()

    constructor(iterable: Iterable<BitString>) : this(iterable.asSequence())
    constructor(vararg bitString: BitString) : this(bitString.asSequence())

    operator fun plus(other: BitPrefix) =
        BitPrefix(prefix.asSequence() + other.asSequence())

    override fun iterator(): Iterator<BitString> = prefix.iterator()

    override fun toString(): String = buildString {
        append('{')
        prefix.forEachIndexed { index, bitString ->
            bitString.forEach {
                append(it.toInt())
            }
            if (index != prefix.indices.last) {
                append(',')
            }
        }
        append('}')
    }

    fun isEmpty(): Boolean = prefix.isEmpty()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as BitPrefix

        if (prefix != other.prefix) return false

        return true
    }

    override fun hashCode(): Int {
        return prefix.hashCode()
    }
}


//class BitPrefix(
//    val prefix: MutableList<ULong> = ArrayList(),
//) {
//    operator fun plusAssign(bitPrefix: BitPrefix) {
//        if (bitPrefix.prefix.isEmpty()) return
//        if (prefix.isEmpty()) {
//            prefix.addAll(bitPrefix.prefix)
//        }
//
//    }
//
//    fun merge(value: ULong) {
//        var z = value
//        if (prefix.isNotEmpty()) {
//            prefix.add(z)
//            return
//        }
//        var w = z.lowerBit
//        while (prefix.isNotEmpty()) {
//            val t = z xor prefix.last()
//            if (t == 0UL) {
//                return
//            }
//            if (t != (w shl 1)) {
//                break
//            }
//            z -= w
//            w = w shl 1
//            prefix.removeLast()
//        }
//        prefix.add(z)
//    }
//}
//
//fun neg(x: Int): Int {
//    return x.inv() + 1
//}
//
//inline val ULong.bitsNegate get() = inv() + 1u
//inline val ULong.lowerBit get() = this and bitsNegate