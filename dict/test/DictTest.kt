package org.ton.dict

import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import kotlin.test.Test
import kotlin.test.assertEquals

class DictTest {
    @Test
    fun testDict() {
        val dict = RawDictionary(32)
        dict.set(
            CellBuilder().storeUInt(0xcafebabe.toInt(), 32).toBitString(),
            CellBuilder().storeUInt(0xdeadbeef.toInt(), 32).build().beginParse()
        )
        println(dict.root)
        assertEquals(
            "9E2B71045A456389525FBC1F34B7ED234BF84B9D21B154461614EFEC92FB99C5",
            dict.root?.hash()?.toHexString()
        )
        dict.set(
            CellBuilder().storeUInt(0xcafebabe.toInt(), 32).toBitString(),
            CellBuilder().storeUInt(0xffff.toInt(), 32).build().beginParse()
        )
        assertEquals(
            "89A396110350B0FB4DDEB4F501BEE0CDF914691CEC838ECC0B7D9839BF2C990A",
            dict.root?.hash()?.toHexString()
        )
    }

    @Test
    fun testDictSetComplex() {
        val value = CellBuilder().storeBit(true).build().beginParse()
        val dict = RawDictionary(32)
        for (i in 0 until 520) {
            val key = CellBuilder().storeUInt(i, 32).toBitString()
            dict.set(key, value)
        }
        assertEquals(
            "9592C8784B8350CC55E75D85D8FF48B122F5A08A01E8ACE32D94732C90BFC032",
            dict.root?.hash()?.toHexString()
        )
        repeat(5) {
            val dict2 = RawDictionary(32)
            for (i in (0 until 520).shuffled()) {
                val key = CellBuilder().storeUInt(i, 32).toBitString()
                dict2.set(key, value)
            }
            assertEquals(dict.root, dict2.root)
        }
    }

    @Test
    fun testBigKey() {
        val value = CellBuilder().storeBit(true).build().beginParse()
        val dict = RawDictionary(32)

        dict.set(CellBuilder().storeUInt(208, 32).toBitString(), value)
        dict.set(CellBuilder().storeUInt(431, 32).toBitString(), value)
        dict.set(CellBuilder().storeUInt(422, 32).toBitString(), value)
        dict.set(CellBuilder().storeUInt(508, 32).toBitString(), value)
        println(dict.root)
    }

    @Test
    fun readLabelTest() {
        val slice = CellBuilder().storeBits(BitString.binary("0111101010")).endCell().beginParse()
        val label = readLabel(slice, 8)
        println(label.toBitString().toBinary())
    }

    @Test
    fun fuzzTest() {
        val value = CellBuilder().storeBit(true).build().beginParse()
        var minFailedIndex = Int.MAX_VALUE
        repeat(10000) {
            val shuffled = (0 until 520).shuffled()
            var currentIndex = 0
            var currentValue = 0
            try {
                val dict = RawDictionary(32)
                shuffled.forEachIndexed { index, i ->
                    currentIndex = index
                    currentValue = i
                    val key = CellBuilder().storeUInt(i, 32).toBitString()
                    dict.set(key, value)
                }
                assertEquals(
                    "9592C8784B8350CC55E75D85D8FF48B122F5A08A01E8ACE32D94732C90BFC032",
                    dict.root?.hash()?.toHexString()
                )
            } catch (e: Throwable) {
                if (currentIndex < minFailedIndex) {
                    minFailedIndex = currentIndex
                    println("\nfailed with key `$currentValue` on `$currentIndex` for $shuffled")
                }
            }
        }
    }

//    @Test
//    fun test120() {
//        val dict = RawDictionary(32)
//        dict.set(1, true)
//        dict.set(2, true)
//        dict.set(0, true)
//        assertEquals(
//            "24847324434282CFB50AC039FFD9EDAE929864EA767338F386302B684F540256",
//            dict.root?.hash()?.toHexString()
//        )
//    }
}