package org.ton.dict

import org.ton.cell.CellBuilder
import org.ton.cell.CellContext
import kotlin.test.Test
import kotlin.test.assertEquals

class DictTest {
    val int32Serializer = { b: CellBuilder, _: CellContext, v: Int ->
        b.storeUInt(v, 32)
        Unit
    }
    val boolSerializer = { b: CellBuilder, _: CellContext, v: Boolean ->
        b.storeBit(v)
        Unit
    }

    @Test
    fun testDict() {
        val dict = Dictionary<Int, Int>(32, int32Serializer, int32Serializer)
        dict.set(0xcafebabe.toInt(), 0xdeadbeef.toInt())
        assertEquals(
            "9E2B71045A456389525FBC1F34B7ED234BF84B9D21B154461614EFEC92FB99C5",
            dict.root?.hash()?.toHexString()
        )
        dict.set(0xcafebabe.toInt(), 0xffff)
        assertEquals(
            "89A396110350B0FB4DDEB4F501BEE0CDF914691CEC838ECC0B7D9839BF2C990A",
            dict.root?.hash()?.toHexString()
        )
    }

    @Test
    fun testDictSetComplex() {
        val dict = Dictionary(32, int32Serializer, boolSerializer)
        for (i in 0 until 520) {
            dict.set(i, true)
        }
        assertEquals(
            "9592C8784B8350CC55E75D85D8FF48B122F5A08A01E8ACE32D94732C90BFC032",
            dict.root?.hash()?.toHexString()
        )
    }

    @Test
    fun test120() {
        val dict = Dictionary(32, int32Serializer, boolSerializer)
        dict.set(1, true)
        dict.set(2, true)
        dict.set(0, true)
        assertEquals(
            "24847324434282CFB50AC039FFD9EDAE929864EA767338F386302B684F540256",
            dict.root?.hash()?.toHexString()
        )
    }

    @Test
    fun test1162() {
        // 3, 16, 15, 2, 0
        val dict = Dictionary(32, int32Serializer, boolSerializer)
        dict.set(3, true)
        dict.set(16, true)
        dict.set(15, true)
        dict.set(2, true)
        dict.set(0, true)
    }

    @Test
    fun fuzzTest() {
        val bad = LinkedHashSet<Pair<Int, List<Int>>>()
        Int.MAX_VALUE
        repeat(1000) {
//            repeat(20) { max ->
            repeat(50000) {
                val set = (0 until 17).shuffled()
                var ii = 0
                try {
                    val dict2 = Dictionary(32, int32Serializer, boolSerializer)
                    set.forEachIndexed { index, value ->
                        ii = index
                        dict2.set(value, false)
                    }
                } catch (e: Throwable) {
                    if (ii == 4 && bad.add(ii to set)) {
                        println("[$ii] bad case: $set")
                    }
                }
            }
//            println("found ${bad.size} bad cases:")
//            bad.filter { it.first == min }.forEach { println(it) }
        }
    }
}