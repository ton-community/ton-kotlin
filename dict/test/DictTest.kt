package org.ton.dict

import kotlinx.io.bytestring.toHexString
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.measureTime

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
            dict.root?.hash()?.toHexString(HexFormat.UpperCase)
        )
        dict.set(
            CellBuilder().storeUInt(0xcafebabe.toInt(), 32).toBitString(),
            CellBuilder().storeUInt(0xffff.toInt(), 32).build().beginParse()
        )
        assertEquals(
            "89A396110350B0FB4DDEB4F501BEE0CDF914691CEC838ECC0B7D9839BF2C990A",
            dict.root?.hash()?.toHexString(HexFormat.UpperCase)
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
            "9592c8784b8350cc55e75d85d8ff48b122f5a08a01e8ace32d94732c90bfc032",
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

    // 19.118s
    // 19.404s
    // 19.250s
    // 19.076s

    // 15.128 make calc of common prefix
    // 15.687
    // 15.206
    // 15.460

    // 15.660 - storeRef without grow
    // 15.445
    // 15.445

    // 13.723 - list to array in computeHashes
    // 13.770
    // 12.882
    // 13.709
    // 13.675

    // 13.241
    // 13.418
    // 13.295
    // 13.268

    // 12.139
    // 12.348
    // 12.369

    // 12.032
    // 12.240
    // 12.183

    // 12.331
    // 12.236

    // 11.244
    // 11.425
    // 11.470

    // 10.671

    // 10.432
    // 10.658
    // 10.592
    @Test
    fun fuzzTest() = measureTime {
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
                    "9592c8784b8350cc55e75d85d8ff48b122f5a08a01e8ace32d94732c90bfc032",
                    dict.root?.hash()?.toHexString()
                )
            } catch (e: Throwable) {
                if (currentIndex < minFailedIndex) {
                    minFailedIndex = currentIndex
                    println("\nfailed with key `$currentValue` on `$currentIndex` for $shuffled")
                }
            }
        }
    }.let {
        println("done for $it")
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