@file:Suppress("NOTHING_TO_INLINE")

package org.ton.block

import io.ktor.utils.io.bits.*
import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.bitstring.Bits256
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.crypto.crc16
import org.ton.crypto.hex
import org.ton.tlb.*
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.jvm.JvmStatic

public inline fun AddrStd(address: String): AddrStd = AddrStd.parse(address)

@Serializable
@SerialName("addr_std")
public data class AddrStd(
    val anycast: Maybe<Anycast>,
    override val workchainId: Int,
    val address: Bits256
) : MsgAddressInt {
    public constructor(workchainId: Int, address: BitString) : this(null, workchainId, address)
    public constructor(workchainId: Int, address: ByteArray) : this(null, workchainId, address)
    public constructor(anycast: Anycast?, workchainId: Int, address: ByteArray) : this(
        anycast.toMaybe(),
        workchainId,
        Bits256(address)
    )

    public constructor(anycast: Anycast?, workchainId: Int, address: BitString) : this(
        anycast.toMaybe(),
        workchainId,
        Bits256(address)
    )

    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("addr_std") {
            field("anycast", anycast)
            field("workchain_id", workchainId)
            field("address", address)
        }
    }

    override fun toString(): String = print().toString()

    public fun toString(
        userFriendly: Boolean = true,
        urlSafe: Boolean = true,
        testOnly: Boolean = false,
        bounceable: Boolean = true
    ): String = toString(this, userFriendly, urlSafe, testOnly, bounceable)

    public companion object : TlbCodec<AddrStd> by AddrStdTlbConstructor {
        @JvmStatic
        public fun tlbCodec(): TlbConstructor<AddrStd> = AddrStdTlbConstructor

        @OptIn(ExperimentalEncodingApi::class)
        @JvmStatic
        public fun toString(
            address: AddrStd,
            userFriendly: Boolean = true,
            urlSafe: Boolean = true,
            testOnly: Boolean = false,
            bounceable: Boolean = true
        ): String {
            return if (userFriendly) {
                val tag = tag(testOnly, bounceable)
                val workchain = address.workchainId
                val rawAddress = address.address.toByteArray()
                val checksum = checksum(tag, workchain, rawAddress)

                val data = buildPacket {
                    writeByte(tag)
                    writeByte(workchain.toByte())
                    writeFully(rawAddress)
                    writeShort(checksum.toShort())
                }.readBytes()

                if (urlSafe) {
                    Base64.UrlSafe.encode(data)
                } else {
                    Base64.encode(data)
                }
            } else {
                "${address.workchainId}:${address.address}"
            }
        }

        @JvmStatic
        public fun parse(address: String): AddrStd = try {
            if (address.contains(':')) {
                parseRaw(address)
            } else {
                parseUserFriendly(address)
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("Can't parse address: $address", e)
        }

        @JvmStatic
        public fun parseRaw(address: String): AddrStd {
            require(address.contains(':'))
            // 32 bytes, each represented as 2 characters
            require(address.substringAfter(':').length == 32 * 2)
            return AddrStd(
                // toByte() to make sure it fits into 8 bits
                workchainId = address.substringBefore(':').toByte().toInt(),
                address = hex(address.substringAfter(':'))
            )
        }

        @OptIn(ExperimentalEncodingApi::class)
        @JvmStatic
        public fun parseUserFriendly(address: String): AddrStd {
            val addressBytes = ByteArray(36)

            try {
                Base64.UrlSafe.decodeIntoByteArray(address, addressBytes)
            } catch (e: Exception) {
                try {
                    Base64.Default.decodeIntoByteArray(address, addressBytes)
                } catch (e: Exception) {
                    throw IllegalArgumentException("Can't parse address: $address", e)
                }
            }
            var tag: Byte = 0
            var workchainId = 0
            var rawAddress = ByteArray(32)
            var expectedChecksum = 0
            addressBytes.useMemory(0, addressBytes.size) {
                tag = it.loadAt(0)
                val cleanTestOnly = tag and 0x7F.toByte()
                check((cleanTestOnly == 0x11.toByte()) or (cleanTestOnly == 0x51.toByte())) {
                    "unknown address tag"
                }
                workchainId = it.loadIntAt(1)
                rawAddress = addressBytes.copyInto(rawAddress, startIndex = 5, endIndex = 5 + 32)
                expectedChecksum = it.loadUShortAt(5 + 32).toInt()
            }

            val actualChecksum = checksum(tag, workchainId, rawAddress)
            check(expectedChecksum == actualChecksum) {
                "CRC check failed"
            }

            return AddrStd(
                workchainId = workchainId,
                address = rawAddress
            )
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
        storeInt(value.workchainId, 8)
        storeBits(value.address)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): AddrStd = cellSlice {
        val anycast = loadTlb(MaybeAnycast)
        val workchainId = loadInt(8).toInt()
        val address = loadBits256()
        AddrStd(anycast, workchainId, address)
    }
}
