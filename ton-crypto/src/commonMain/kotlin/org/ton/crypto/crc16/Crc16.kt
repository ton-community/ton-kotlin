package org.ton.crypto.crc16

import org.ton.crypto.reverse
import kotlin.experimental.xor

enum class Crc16(
    val poly: Int,
    val init: Short,
    val refIn: Boolean,
    val refOut: Boolean,
    val xorOut: Short,
    val check: Int
) {
    ARC(0x8005, 0x0000, true, true, 0x0000, 0xBB3D),
    AUG_CCITT(0x1021, 0x1D0F, false, false, 0x0000, 0xE5CC),
    BUYPASS(0x8005, 0x0000, false, false, 0x0000, 0xFEE8),
    CCITT_FALSE(0x1021, 0xFFFF.toShort(), false, false, 0x0000, 0x29B1),
    CDMA2000(0xC867, 0xFFFF.toShort(), false, false, 0x0000, 0x4C06),
    DDS_110(0x8005, 0x800D.toShort(), false, false, 0x0000, 0x9ECF),
    DECT_R(0x0589, 0x0000, false, false, 0x0001, 0x007E),
    DECT_X(0x0589, 0x0000, false, false, 0x0000, 0x007F),
    DNP(0x3D65, 0x0000, true, true, 0xFFFF.toShort(), 0xEA82),
    EN_13757(0x3D65, 0x0000, false, false, 0xFFFF.toShort(), 0xC2B7),
    GENIBUS(0x1021, 0xFFFF.toShort(), false, false, 0xFFFF.toShort(), 0xD64E),
    MAXIM(0x8005, 0x0000, true, true, 0xFFFF.toShort(), 0x44C2),
    MCRF4XX(0x1021, 0xFFFF.toShort(), true, true, 0x0000, 0x6F91),
    RIELLO(0x1021, 0xB2AA.toShort(), true, true, 0x0000, 0x63D0),
    T10_DIF(0x8BB7, 0x0000, false, false, 0x0000, 0xD0DB),
    TELEDISK(0xA097, 0x0000, false, false, 0x0000, 0x0FB3),
    TMS37157(0x1021, 0x89EC.toShort(), true, true, 0x0000, 0x26B1),
    USB(0x8005, 0xFFFF.toShort(), true, true, 0xFFFF.toShort(), 0xB4C8),
    CRC_A(0x1021, 0xC6C6.toShort(), true, true, 0x0000, 0xBF05),
    KERMIT(0x1021, 0x0000, true, true, 0x0000, 0x2189),
    MODBUS(0x8005, 0xFFFF.toShort(), true, true, 0x0000, 0x4B37),
    X_25(0x1021, 0xFFFF.toShort(), true, true, 0xFFFF.toShort(), 0x906E),
    XMODEM(0x1021, 0x0000, false, false, 0x0000, 0x31C3);

    override fun toString(): String = "CRC-16/$name"

    operator fun invoke(data: ByteArray) = checksum(data)

    fun checksum(data: ByteArray): Short {
        val table = makeTable()
        val crc = update(init, data, table)
        return complete(crc)
    }

    private fun makeTable() = IntArray(256) { n ->
        var crc = n shl 8
        repeat(8) {
            val bit = (crc and 0x8000) != 0
            crc = crc shl 1
            if (bit) {
                crc = crc xor poly
            }
        }
        crc
    }

    @Suppress("NAME_SHADOWING")
    private fun update(crc: Short, data: ByteArray, table: IntArray): Short {
        var crc = crc
        data.forEach { d ->
            var d = d
            if (refIn) {
                d = d.reverse()
            }
            crc = (crc.toInt() shl 8).toShort() xor table[(crc.toInt() shr 8) xor d.toInt()].toShort()
        }
        return crc
    }

    private fun complete(crc: Short): Short {
        if (refOut) {
            return crc.reverse() xor xorOut
        }
        return crc xor xorOut
    }
}
