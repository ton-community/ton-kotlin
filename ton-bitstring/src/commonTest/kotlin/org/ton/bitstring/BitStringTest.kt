package org.ton.bitstring

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.fail

class BitStringTest {
    @Test
    fun bitString_10001010_8a() {
        val bitString = BitString(byteArrayOf(0b1000_1010.toByte()))
        assertEquals("10001010", bitString.toBooleanArray().joinToBits())
        assertEquals("8A", bitString.toString())
    }

    @Test
    fun bitString_8a_10001010() {
        val bitString1 = BitString("8A")
        assertEquals("10001010", bitString1.toBooleanArray().joinToBits())
        assertEquals("8A", bitString1.toString())

        val bitString2 = BitString.binary("10001010")
        assertEquals("10001010", bitString2.toBooleanArray().joinToBits())
        assertEquals("8A", bitString2.toString())
    }

    @Test
    fun bitString_100010_8a_() {
        val bitString = BitString(
                true,
                false,
                false,
                false,
                true,
                false,
        )
        assertEquals("100010", bitString.toBooleanArray().joinToBits())
        assertEquals("8A_", bitString.toString())
    }

    @Test
    fun bitString_8a__100010() {
        val bitString1 = BitString("8A_")
        assertEquals("100010", bitString1.toBooleanArray().joinToBits())
        assertEquals("8A_", bitString1.toString())

        val bitString2 = BitString.binary("100010")
        assertEquals("100010", bitString2.toBooleanArray().joinToBits())
        assertEquals("8A_", bitString2.toString())
    }

    @Test
    fun bitString_00101101100_2d9() {
        val bitString = BitString(
                false, false, true, false, true, true, false, true, true, false, false
        )
        assertEquals("00101101100", bitString.toBooleanArray().joinToBits())
        assertEquals("2D9_", bitString.toString())
    }

    @Test
    fun bitString_2D9__00101101100() {
        val bitString1 = BitString("2D9_")
        assertEquals("00101101100", bitString1.toBooleanArray().joinToBits())
        assertEquals("2D9_", bitString1.toString())

        val bitString2 = BitString.binary("00101101100")
        assertEquals("00101101100", bitString2.toBooleanArray().joinToBits())
        assertEquals("2D9_", bitString2.toString())
    }

    @Test
    fun `BitString 1_ 000`() {
        val bitString1 = BitString.binary("000")
        assertEquals("000", bitString1.toBooleanArray().joinToBits())
        assertEquals("1_", bitString1.toString())

        val bitString2 = BitString.binary("1_")
        assertEquals("000", bitString2.toBooleanArray().joinToBits())
        assertEquals("1_", bitString2.toString())
    }

    @Test
    fun `random BitString`() {
        repeat(100) {
            repeat(BitString.MAX_LENGTH) { length ->
                val bits1 = BooleanArray(length) { Random.nextBoolean() }
                try {
                    val bitString1 = BitString(*bits1)

                    val bits2 = bitString1.toBooleanArray()
                    assertContentEquals(bits1, bits2)

                    val bytes1 = bitString1.toByteArray()
                    val bitString2 = BitString(length, bytes1)

                    assertEquals(bitString1, bitString2)
                    assertEquals(bitString1.toString(), bitString2.toString())
                    assertEquals(bitString1.toBooleanArray().joinToBits(), bitString2.toBooleanArray().joinToBits())
                    assertContentEquals(bitString1.toByteArray(), bitString2.toByteArray())
                } catch (e: Exception) {
                    fail("bits: ${bits1.joinToBits()}", e)
                }
            }
        }
    }

    @Test
    fun `toString() on a zero number`() {
        assertEquals("0", BitString(List(4, { false })).toString())
        assertEquals("00000000", BitString(List(32, { false })).toString())
        assertEquals("0000000000000000", BitString(List(64, { false })).toString())
    }
}

private fun BooleanArray.joinToBits() = joinToString(separator = "") { if (it) "1" else "0" }
