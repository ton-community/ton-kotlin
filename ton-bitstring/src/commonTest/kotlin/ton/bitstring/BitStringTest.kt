package ton.bitstring

import kotlin.test.Test
import kotlin.test.assertEquals

class BitStringTest {
    @Test
    fun bitString_10001010_8a() {
        val bitString = BitString(8)
        // 10001010
        bitString.writeBit(true)
        bitString.writeBit(false)
        bitString.writeBit(false)
        bitString.writeBit(false)
        bitString.writeBit(true)
        bitString.writeBit(false)
        bitString.writeBit(true)
        bitString.writeBit(false)
        assertEquals("8A", bitString.toString())
    }

    @Test
    fun bitString_100010_8a_() {
        val bitString = BitString(6)
        // 100010
        bitString.writeBit(true)
        bitString.writeBit(false)
        bitString.writeBit(false)
        bitString.writeBit(false)
        bitString.writeBit(true)
        bitString.writeBit(false)
        assertEquals("8A_", bitString.toString())
    }

    @Test
    fun bitString_00101101100_2d9() {
        val bitString = BitString(11)
        //00101101100
        bitString.writeBit(false)
        bitString.writeBit(false)
        bitString.writeBit(true)
        bitString.writeBit(false)
        bitString.writeBit(true)
        bitString.writeBit(true)
        bitString.writeBit(false)
        bitString.writeBit(true)
        bitString.writeBit(true)
        bitString.writeBit(false)
        bitString.writeBit(false)
        assertEquals("2D9_", bitString.toString())
    }

    @Test
    fun testInt() {
        val bitString = BitString(64)
        bitString.writeUInt(6u, 5)
        bitString.writeUInt(3u, 4)
        bitString.writeUInt(91111u, 32)

        val reader = BitStringReader(bitString)
        val actualBitString = BitString(64).apply {
            writeUInt(reader.readUInt(5), 5)
            writeUInt(reader.readUInt(4), 4)
            writeUInt(reader.readUInt(32), 32)
        }

        assertEquals(bitString, actualBitString)
    }
}

