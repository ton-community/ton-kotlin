@file:Suppress("NOTHING_TO_INLINE")

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bigint.toBigInt
import org.ton.bitstring.BitString
import org.ton.bitstring.toBitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.crypto.base64
import org.ton.crypto.base64url
import org.ton.crypto.crc16
import org.ton.crypto.hex
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb
import kotlin.experimental.and
import kotlin.experimental.or

inline fun AddrStd(address: String): AddrStd = AddrStd.parse(address)

@Serializable
@SerialName("addr_std")
data class AddrStd(
    val anycast: Maybe<Anycast>,
    override val workchainId: Int,
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

    fun toString(
        userFriendly: Boolean = true,
        urlSafe: Boolean = true,
        testOnly: Boolean = false,
        bounceable: Boolean = true
    ): String = toString(this, userFriendly, urlSafe, testOnly, bounceable)

    companion object {
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
                val raw = byteArrayOf(tag(testOnly, bounceable), address.workchainId.toByte()) +
                        address.address.toByteArray() + crc(address, testOnly, bounceable).toShort().toBigInt()
                    .toByteArray()
                if (urlSafe) {
                    base64url(raw)
                } else {
                    base64(raw)
                }
            } else {
                address.workchainId.toString() + ":" + hex(address.address.toByteArray())
            }
        }

        @JvmStatic
        fun parse(address: String): AddrStd {
            return if (address.contains(':')) {
                parseRaw(address)
            } else {
                parseUserFriendly(address)
            }
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
            val raw = try {
                base64url(address)
            } catch (E: Exception) {
                base64(address)
            }

            require(raw.size == 36)
            return AddrStd(
                workchainId = raw[1].toInt(),
                address = raw.sliceArray(2..33)
            ).apply {
                val testOnly = raw[0] and 0x80.toByte() != 0.toByte()
                if (testOnly) {
                    // not 0x80 = 0x7F; here we clean the test only flag
                    raw[0] = raw[0] and 0x7F.toByte()
                }

                check((raw[0] == 0x11.toByte()) or (raw[0] == 0x51.toByte())) { "unknown address tag" }

                val bounceable = raw[0] == 0x11.toByte()
                check(
                    (crc(
                        this,
                        testOnly,
                        bounceable
                    ) == raw[34].toUByte().toInt() * 256 + raw[35].toUByte().toInt())
                ) { "CRC check failed" }
            }
        }

        private fun crc(address: AddrStd, testOnly: Boolean, bounceable: Boolean): Int =
            crc16(
                byteArrayOf(tag(testOnly, bounceable), address.workchainId.toByte()),
                address.address.toByteArray()
            )

        // Get the tag byte based on set flags
        private fun tag(testOnly: Boolean, bounceable: Boolean): Byte =
            (if (testOnly) 0x80.toByte() else 0.toByte()) or
                    (if (bounceable) 0x11.toByte() else 0x51.toByte())
    }
}

private object AddrStdTlbConstructor : TlbConstructor<AddrStd>(
    schema = "addr_std\$10 anycast:(Maybe Anycast) workchain_id:int8 address:bits256 = MsgAddressInt;"
) {
    private val maybeAnycastCodec by lazy {
        Maybe.tlbCodec(Anycast.tlbCodec())
    }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: AddrStd
    ) = cellBuilder {
        storeTlb(maybeAnycastCodec, value.anycast)
        storeInt(value.workchainId, 8)
        storeBits(value.address)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): AddrStd = cellSlice {
        val anycast = loadTlb(maybeAnycastCodec)
        val workchainId = loadInt(8).toInt()
        val address = loadBitString(256)
        AddrStd(anycast, workchainId, address)
    }
}
