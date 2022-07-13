package org.ton.tl

import io.ktor.utils.io.core.*
import org.ton.crypto.hex
import org.ton.tl.constructors.readBytesTl
import org.ton.tl.constructors.writeBytesTl
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertTrue

class BytesTlConstructorTest {
    @Test
    fun `test bytes`() {
        assertBytes("00 00 00 00", "")
        assertBytes("01 FF 00 00", "FF")
        assertBytes("02 FF FF 00", "FF FF")
        assertBytes("03 FF FF FF", "FF FF FF")
        assertBytes("04 FF FF FF FF 00 00 00", "FF FF FF FF")
        assertBytes("05 FF FF FF FF FF 00 00", "FF FF FF FF FF")
        assertBytes("06 FF FF FF FF FF FF 00", "FF FF FF FF FF FF")
        assertBytes("07 FF FF FF FF FF FF FF", "FF FF FF FF FF FF FF")
    }

    @Test
    fun `fuzz test bytes`() {
        repeat(25) {
            val size = 1 shl it
            val bytes = Random.nextBytes(size)
            assertEncoding(bytes)
        }
    }

    fun assertBytes(data: String, expected: String) {
        val serializedBytes = hex(data.replace(" ", ""))
        val expectedBytes = hex(expected.replace(" ", ""))
        val reader = ByteReadPacket(serializedBytes)
        val actualReadBytes = reader.readBytesTl()
        assertContentEquals(expectedBytes, actualReadBytes)
        assertTrue(reader.isEmpty)
        val actualWriteBytes = buildPacket {
            writeBytesTl(expectedBytes)
        }.readBytes()
        assertTrue(actualWriteBytes.size % 4 == 0)
        assertContentEquals(serializedBytes, actualWriteBytes)
    }

    fun assertEncoding(data: ByteArray) {
        val serializedBytes = buildPacket {
            writeBytesTl(data)
        }
        assertTrue(serializedBytes.remaining % 4 == 0L)
        val deserializedBytes = serializedBytes.readBytesTl()
        assertTrue(serializedBytes.isEmpty)
        assertContentEquals(data, deserializedBytes)
    }
}