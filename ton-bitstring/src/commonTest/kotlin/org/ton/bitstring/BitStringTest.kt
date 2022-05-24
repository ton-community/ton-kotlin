package org.ton.bitstring

import kotlin.test.Test
import kotlin.test.assertEquals

class BitStringTest {
    @Test
    fun bitString_10001010_8a() {
        val bitString = BitString(byteArrayOf(0b1000_1010.toByte()))
        assertEquals("10001010", bitString.toBooleanArray().joinToBits())
        assertEquals("8A", bitString.toString())
    }

    @Test
    fun bitString_8a_10001010() {
        val bitString = BitString("8A")
        assertEquals("10001010", bitString.toBooleanArray().joinToBits())
        assertEquals("8A", bitString.toString())
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
        val bitString = BitString("8A_")
        assertEquals("100010", bitString.toBooleanArray().joinToBits())
        assertEquals("8A_", bitString.toString())
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
        val bitString = BitString("2D9_")
        assertEquals("00101101100", bitString.toBooleanArray().joinToBits())
        assertEquals("2D9_", bitString.toString())
    }
}

private fun BooleanArray.joinToBits() = joinToString(separator = "") { if (it) "1" else "0" }
