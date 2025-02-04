package org.ton.dict

import kotlinx.io.bytestring.toHexString
import org.ton.bitstring.BitString
import org.ton.boc.BagOfCells
import org.ton.cell.CellBuilder
import kotlin.io.encoding.Base64
import kotlin.test.*

class DictTest {
    @Test
    fun testDict() {
        val dict = RawDictionary(32)
        dict.set(
            CellBuilder().storeUInt(0xcafebabe.toUInt(), 32).toBitString(),
            CellBuilder().storeUInt(0xdeadbeef.toUInt(), 32).build().beginParse()
        )
        println(dict.root)
        assertEquals(
            "9E2B71045A456389525FBC1F34B7ED234BF84B9D21B154461614EFEC92FB99C5",
            dict.root?.hash()?.toHexString(HexFormat.UpperCase)
        )
        dict.set(
            CellBuilder().storeUInt(0xcafebabe.toUInt(), 32).toBitString(),
            CellBuilder().storeUInt(0xffff.toUInt(), 32).build().beginParse()
        )
        assertEquals(
            "89A396110350B0FB4DDEB4F501BEE0CDF914691CEC838ECC0B7D9839BF2C990A",
            dict.root?.hash()?.toHexString(HexFormat.UpperCase)
        )
    }

    @Test
    fun testDictSetComplex() {
        val value = CellBuilder().storeBoolean(true).build().beginParse()
        val dict = RawDictionary(32)
        for (i in 0 until 520) {
            val key = CellBuilder().storeInt(i, 32).toBitString()
            assertNull(dict.set(key, value))
        }
        assertEquals(
            "9592c8784b8350cc55e75d85d8ff48b122f5a08a01e8ace32d94732c90bfc032",
            dict.root?.hash()?.toHexString()
        )
        repeat(5) {
            val dict2 = RawDictionary(32)
            for (i in (0 until 520).shuffled()) {
                val key = CellBuilder().storeInt(i, 32).toBitString()
                dict2.set(key, value)
            }
            assertEquals(dict.root, dict2.root)
        }
    }

    @Test
    fun testSetOldValue() {
        val value0 = CellBuilder().storeInt(0, 8).build().beginParse()
        val value1 = CellBuilder().storeInt(0xFF, 8).build().beginParse()
        val key = CellBuilder().storeInt(0, 32).toBitString()
        val dict = RawDictionary(32)
        assertNull(dict.set(key, value0))
        assertEquals(value0.preloadBitString(), dict.set(key, value1)?.preloadBitString())
    }

    @Test
    fun testBigKey() {
        val value = CellBuilder().storeBoolean(true).build().beginParse()
        val dict = RawDictionary(32)

        dict.set(CellBuilder().storeUInt(208u, 32).toBitString(), value)
        dict.set(CellBuilder().storeUInt(431u, 32).toBitString(), value)
        dict.set(CellBuilder().storeUInt(422u, 32).toBitString(), value)
        dict.set(CellBuilder().storeUInt(508u, 32).toBitString(), value)
        println(dict.root)
    }

    @Test
    fun readLabelTest() {
        val slice = CellBuilder().storeBits(BitString.binary("0111101010")).endCell().beginParse()
        val label = readLabel(slice, 8)
        println(label.toBitString().toBinary())
    }

//    @Test
//    fun fuzzTest() {
//        val value = CellBuilder().storeBoolean(true).build().beginParse()
//        var minFailedIndex = Int.MAX_VALUE
//        val iterations = 10000
//        measureTime {
//            repeat(iterations) {
//                val shuffled = (0 until 520).shuffled()
//                var currentIndex = 0
//                var currentValue = 0
//                try {
//                    val dict = RawDictionary(32)
//                    shuffled.forEachIndexed { index, i ->
//                        currentIndex = index
//                        currentValue = i
//                        val key = CellBuilder().storeUInt(i.toUInt(), 32).toBitString()
//                        dict.set(key, value)
//                    }
//                    assertEquals(
//                        "9592c8784b8350cc55e75d85d8ff48b122f5a08a01e8ace32d94732c90bfc032",
//                        dict.root?.hash()?.toHexString()
//                    )
//                } catch (e: Throwable) {
//                    if (currentIndex < minFailedIndex) {
//                        minFailedIndex = currentIndex
//                        println("\nfailed with key `$currentValue` on `$currentIndex` for $shuffled")
//                    }
//                }
//            }
//        }.let {
//            println("score ${iterations / it.toDouble(DurationUnit.SECONDS)}")
//        }
//    }

    @Test
    fun testIterator() {
        val cell =
            BagOfCells(Base64.decode("te6ccgEBFAEAeAABAcABAgPOQAUCAgHUBAMACQAAAI3gAAkAAACjoAIBIA0GAgEgCgcCASAJCAAJAAAAciAACQAAAIfgAgEgDAsACQAAAFZgAAkAAABsIAIBIBEOAgEgEA8ACQAAADqgAAkAAABQYAIBIBMSAAkAAAAe4AAJAAAAv2A=")).roots.first()
        val dict = RawDictionary(cell.asCellSlice().loadRef(), 32)
        val iterator = dict.iterator()

        repeat(10) {
            assertTrue(iterator.hasNext())
            println(iterator.next())
        }

        val last = iterator.hasNext()
        assertFalse(last)

        assertFails {
            iterator.next()
        }
    }

    @Test
    fun testGet() {
        val cell =
            BagOfCells(Base64.decode("te6ccgEBFAEAeAABAcABAgPOQAUCAgHUBAMACQAAAI3gAAkAAACjoAIBIA0GAgEgCgcCASAJCAAJAAAAciAACQAAAIfgAgEgDAsACQAAAFZgAAkAAABsIAIBIBEOAgEgEA8ACQAAADqgAAkAAABQYAIBIBMSAAkAAAAe4AAJAAAAv2A=")).roots.first()
        val dict = RawDictionary(cell.asCellSlice().loadRef(), 32)

        dict.forEach { (key, value) ->
            val byGet = dict.get(key)
            assertEquals(value, byGet)
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