package ton.bitstring

import kotlin.test.Test
import kotlin.test.assertEquals

class BitStringTest {
    @Test
    fun bitString_10001010_8a() {
        // 10001010
        val bitString = buildBitString {
            writeBit(true)
            writeBit(false)
            writeBit(false)
            writeBit(false)
            writeBit(true)
            writeBit(false)
            writeBit(true)
            writeBit(false)
        }

        assertEquals("8A", bitString.toString())
    }

    @Test
    fun bitString_100010_8a_() {
        // 100010
        val bitString = buildBitString {
            writeBit(true)
            writeBit(false)
            writeBit(false)
            writeBit(false)
            writeBit(true)
            writeBit(false)
        }

        assertEquals("8A_", bitString.toString())
    }

    @Test
    fun bitString_00101101100_2d9() {
        //00101101100
        val bitString = buildBitString {
            writeBit(false)
            writeBit(false)
            writeBit(true)
            writeBit(false)
            writeBit(true)
            writeBit(true)
            writeBit(false)
            writeBit(true)
            writeBit(true)
            writeBit(false)
            writeBit(false)
        }

        assertEquals("2D9_", bitString.toString())
    }

    @Test
    fun testInt() {
        val bitString = buildBitString {
            writeUInt(6u, 5)
            writeUInt(3u, 4)
            writeUInt(91111u, 32)
        }

        val reader = BitStringReader(bitString)
        val actualBitString = buildBitString {
            writeUInt(reader.readUInt(5), 5)
            writeUInt(reader.readUInt(4), 4)
            writeUInt(reader.readUInt(32), 32)
        }

        assertEquals(bitString, actualBitString)
    }
}

