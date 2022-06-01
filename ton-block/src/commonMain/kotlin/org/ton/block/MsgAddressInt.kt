package org.ton.block

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.bigint.toBigInt
import org.ton.bitstring.BitString
import org.ton.bitstring.toBitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.crypto.*
import org.ton.tlb.*
import kotlin.experimental.and
import kotlin.experimental.or

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("@type")
@Serializable
sealed interface MsgAddressInt : MsgAddress {
    @SerialName("addr_std")
    @Serializable
    data class AddrStd(
        val anycast: Maybe<Anycast>,
        @SerialName("workchain_id")
        val workchainId: Int,
        @Serializable(HexByteArraySerializer::class)
        val address: ByteArray
    ) : MsgAddressInt {
        constructor(workchainId: Int, address: ByteArray) : this(null, workchainId, address)
        constructor(anycast: Anycast?, workchainId: Int, address: ByteArray) : this(
            anycast.toMaybe(),
            workchainId,
            address
        )

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as AddrStd

            if (anycast != other.anycast) return false
            if (workchainId != other.workchainId) return false
            if (!address.contentEquals(other.address)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = anycast.hashCode()
            result = 31 * result + workchainId
            result = 31 * result + address.contentHashCode()
            return result
        }

        override fun toString() = Json.encodeToString(serializer(), this)

        fun toString(
            userFriendly: Boolean = true,
            urlSafe: Boolean = true,
            testOnly: Boolean = false,
            bounceable: Boolean = true
        ): String = if (userFriendly) {
            val raw = byteArrayOf(tag(testOnly, bounceable), workchainId.toByte()) +
                    address + crc(this, testOnly, bounceable).toShort().toBigInt().toByteArray()
            if (urlSafe) {
                base64url(raw)
            } else {
                base64(raw)
            }
        } else {
            workchainId.toString() + ":" + hex(address)
        }

        companion object {
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

            @JvmStatic
            private fun crc(address: AddrStd, testOnly: Boolean, bounceable: Boolean): Int =
                crc16(
                    byteArrayOf(tag(testOnly, bounceable), address.workchainId.toByte()),
                    address.address
                )

            @JvmStatic
            // Get the tag byte based on set flags
            private fun tag(testOnly: Boolean, bounceable: Boolean): Byte =
                (if (testOnly) 0x80.toByte() else 0.toByte()) or
                        (if (bounceable) 0x11.toByte() else 0x51.toByte())
        }
    }

    @SerialName("addr_var")
    @Serializable
    data class AddrVar(
        val anycast: Maybe<Anycast>,
        val addrLen: Int,
        val workchainId: Int,
        val address: BitString
    ) : MsgAddressInt {
        constructor(workchainId: Int, address: BitString) : this(null, workchainId, address)
        constructor(anycast: Anycast?, workchainId: Int, address: BitString) : this(
            anycast.toMaybe(),
            address.size,
            workchainId,
            address
        )
    }

    companion object {
        @JvmStatic
        fun tlbCodec(): TlbCodec<MsgAddressInt> = MsgAddressIntTlbCombinator()

        internal class MsgAddressIntTlbCombinator : TlbCombinator<MsgAddressInt>() {
            private val addrStdConstructor by lazy {
                AddrStdTlbConstructor()
            }
            private val addrVarConstructor by lazy {
                AddrVarTlbConstructor()
            }

            override val constructors: List<TlbConstructor<out MsgAddressInt>> by lazy {
                listOf(addrStdConstructor, addrVarConstructor)
            }

            override fun getConstructor(value: MsgAddressInt): TlbConstructor<out MsgAddressInt> = when (value) {
                is AddrStd -> addrStdConstructor
                is AddrVar -> addrVarConstructor
            }

            internal class AddrStdTlbConstructor : TlbConstructor<AddrStd>(
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
                    storeBits(value.address.toBitString())
                }

                override fun loadTlb(
                    cellSlice: CellSlice
                ): AddrStd = cellSlice {
                    val anycast = loadTlb(maybeAnycastCodec)
                    val workchainId = loadInt(8).toInt()
                    val address = loadBitString(256).toByteArray()
                    AddrStd(anycast, workchainId, address)
                }
            }

            internal class AddrVarTlbConstructor : TlbConstructor<AddrVar>(
                schema = "addr_var\$11 anycast:(Maybe Anycast) addr_len:(## 9) workchain_id:int32 address:(bits addr_len) = MsgAddressInt;"
            ) {
                private val maybeAnycastCodec by lazy {
                    Maybe.tlbCodec(Anycast.tlbCodec())
                }

                override fun storeTlb(
                    cellBuilder: CellBuilder,
                    value: AddrVar
                ) = cellBuilder {
                    storeTlb(maybeAnycastCodec, value.anycast)
                    storeUInt(value.addrLen, 9)
                    storeInt(value.workchainId, 32)
                    storeBits(value.address)
                }

                override fun loadTlb(
                    cellSlice: CellSlice
                ): AddrVar = cellSlice {
                    val anycast = loadTlb(maybeAnycastCodec)
                    val addrLen = loadUInt(9).toInt()
                    val workchainId = loadInt(32).toInt()
                    val address = loadBitString(addrLen)
                    AddrVar(anycast, addrLen, workchainId, address)
                }
            }
        }
    }
}

