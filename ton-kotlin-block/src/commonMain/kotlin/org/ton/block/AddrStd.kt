@file:Suppress("NOTHING_TO_INLINE")

package org.ton.block

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.bitstring.toBitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.crypto.base64.base64
import org.ton.crypto.base64.base64url
import org.ton.crypto.crc16.crc16
import org.ton.crypto.hex
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.jvm.JvmStatic

inline fun AddrStd(address: String): AddrStd = AddrStd.parse(address)

@Serializable
@SerialName("addr_std")
data class AddrStd(
    val anycast: Maybe<Anycast>,
    override val workchain_id: Int,
    val address: BitString
) : MsgAddressInt {
    init {
        require(address.size == 256) { "address.size expected: 256 actual: ${address.size}" }
    }

    constructor(workchainId: Int, address: BitString) : this(null, workchainId, address)
    constructor(workchainId: Int, address: ByteArray) : this(null, workchainId, address)
    constructor(anycast: Anycast?, workchainId: Int, address: ByteArray) : this(
        anycast.toMaybe(),
        workchainId,
        address.toBitString()
    )

    constructor(anycast: Anycast?, workchainId: Int, address: BitString) : this(
        anycast.toMaybe(),
        workchainId,
        address
    )

    override fun toString(): String = "addr_std(anycast:$anycast workchain_id:$workchain_id address:$address)"

    fun toString(
        userFriendly: Boolean = true,
        urlSafe: Boolean = true,
        testOnly: Boolean = false,
        bounceable: Boolean = true
    ): String = toString(this, userFriendly, urlSafe, testOnly, bounceable)

    companion object : TlbCodec<AddrStd> by AddrStdTlbConstructor {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<AddrStd> = AddrStdTlbConstructor

        @JvmStatic
        fun toString(
            address: AddrStd,
            userFriendly: Boolean = true,
            urlSafe: Boolean = true,
            testOnly: Boolean = false,
            bounceable: Boolean = true
        ): String {
            return if (userFriendly) {
                val tag = tag(testOnly, bounceable)
                val workchain = address.workchain_id
                val rawAddress = address.address.toByteArray()
                val checksum = checksum(tag, workchain, rawAddress)

                val data = buildPacket {
                    writeByte(tag)
                    writeByte(workchain.toByte())
                    writeFully(rawAddress)
                    writeShort(checksum.toShort())
                }.readBytes()

                if (urlSafe) {
                    base64url(data)
                } else {
                    base64(data)
                }
            } else {
                "${address.workchain_id}:${address.address}"
            }
        }

        @JvmStatic
        fun parse(address: String): AddrStd = try {
            if (address.contains(':')) {
                parseRaw(address)
            } else {
                parseUserFriendly(address)
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("Can't parse address: $address", e)
        }

        @JvmStatic
        fun parseRaw(address: String): AddrStd {
            require(address.contains(':'))
            // 32 bytes, each represented as 2 characters
            require(address.substringAfter(':').length == 32 * 2)
            return AddrStd(
                // toByte() to make sure it fits into 8 bits
                workchainId = address.substringBefore(':').toByte().toInt(),
                address = hex(address.substringAfter(':'))
            )
        }

        @JvmStatic
        fun parseUserFriendly(address: String): AddrStd {
            val packet = ByteReadPacket(
                try {
                    base64url(address)
                } catch (e: Exception) {
                    try {
                        base64(address)
                    } catch (e: Exception) {
                        throw IllegalArgumentException("Can't parse address: $address", e)
                    }
                }
            )

            require(packet.remaining == 36L) { "invalid byte-array size expected: 36, actual: ${packet.remaining}" }
            // not 0x80 = 0x7F; here we clean the test only flag to only check proper bounce flags
            val tag = packet.readByte()
            val workchain = packet.readByte().toInt()
            val rawAddress = packet.readBytes(32)
            val cleanTestOnly = tag and 0x7F.toByte()
            check((cleanTestOnly == 0x11.toByte()) or (cleanTestOnly == 0x51.toByte())) {
                "unknown address tag"
            }

            val addrStd = AddrStd(
                workchainId = workchain,
                address = rawAddress
            )

            val expectedChecksum = packet.readUShort().toInt()
            val actualChecksum = checksum(tag, workchain, rawAddress)
            check(expectedChecksum == actualChecksum) {
                "CRC check failed"
            }

            return addrStd
        }

        private fun checksum(tag: Byte, workchainId: Int, address: ByteArray): Int =
            crc16(byteArrayOf(tag, workchainId.toByte()), address)

        // Get the tag byte based on set flags
        private fun tag(testOnly: Boolean, bounceable: Boolean): Byte =
            (if (testOnly) 0x80.toByte() else 0.toByte()) or
                    (if (bounceable) 0x11.toByte() else 0x51.toByte())
    }
}

private object AddrStdTlbConstructor : TlbConstructor<AddrStd>(
    schema = "addr_std\$10 anycast:(Maybe Anycast) workchain_id:int8 address:bits256 = MsgAddressInt;"
) {
    private val MaybeAnycast = Maybe(Anycast)

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: AddrStd
    ) = cellBuilder {
        storeTlb(MaybeAnycast, value.anycast)
        storeInt(value.workchain_id, 8)
        storeBits(value.address)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): AddrStd = cellSlice {
        val anycast = loadTlb(MaybeAnycast)
        val workchainId = loadInt(8).toInt()
        val address = loadBits(256)
        AddrStd(anycast, workchainId, address)
    }
}
