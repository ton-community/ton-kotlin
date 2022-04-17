package org.ton.bitstring

import kotlin.test.Test
import kotlin.test.assertEquals

class BitStringTest {
    @Test
    fun bitString_10001010_8a() {
        val bitString = BitString(byteArrayOf(0b1000_1010.toByte()))
        assertEquals("10001010", bitString.toString())
        assertEquals("8A", bitString.toFiftHex())
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
        assertEquals("100010", bitString.toString())
        assertEquals("8A_", bitString.toFiftHex())
    }

    @Test
    fun bitString_00101101100_2d9() {
        val bitString = BitString(
            false, false, true, false, true, true, false, true, true, false, false
        )
        assertEquals("00101101100", bitString.toString())
        assertEquals("2D9_", bitString.toFiftHex())
    }
}

