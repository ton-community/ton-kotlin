@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package org.ton.block

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.bigint.BigInt
import org.ton.bigint.toBigInt
import org.ton.crypto.HexByteArraySerializer
import org.ton.crypto.base64
import org.ton.crypto.crc16
import org.ton.crypto.hex
import kotlin.experimental.and
import kotlin.experimental.or

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("@type")
@Serializable
sealed interface MsgAddressInt {
    @SerialName("addr_std")
    @Serializable
    data class AddrStd(
            val anycast: Anycast?,
            val workchain_id: Int,
            @Serializable(HexByteArraySerializer::class)
            val address: ByteArray
    ) : MsgAddressInt {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as AddrStd

            if (anycast != other.anycast) return false
            if (workchain_id != other.workchain_id) return false
            if (!address.contentEquals(other.address)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = anycast?.hashCode() ?: 0
            result = 31 * result + workchain_id
            result = 31 * result + address.contentHashCode()
            return result
        }

        override fun toString() = buildString {
            append("MsgAddressInt.AddrStd(anycast=")
            append(anycast)
            append(", workchainId=")
            append(workchain_id)
            append(", address=")
            append(hex(address))
            append(")")
        }

        companion object {
            var userFriendly: Boolean = true
            var urlSafe: Boolean = true
            var testOnly: Boolean = false
            var bounceable: Boolean = false
            @JvmStatic
            fun parse(address: String) : AddrStd {
                if(address.contains(':')) {
                    return parseRaw(address)
                } else {
                    return parseUserFriendly(address)
                }
            }

            @JvmStatic
            fun parseRaw(address: String): AddrStd {
                require(address.contains(':'))
                require(address.substringAfter(':').length == 32)
                return AddrStd(
                    anycast = null,
                    workchain_id = address.substringBefore(':').toByte().toInt(),
                    // toByte() to make sure it fits into 8 bits
                    address = hex(address.substringAfter(':'))
                ).apply {
                    userFriendly = false
                    urlSafe = false
                    testOnly = false
                    bounceable = false
                }
            }

            @JvmStatic
            fun parseUserFriendly(address: String): AddrStd {
                var raw:ByteArray
                raw = base64(address)

                require(raw.size == 36)
                return AddrStd(
                    anycast = null,
                    workchain_id = raw[1].toInt(),
                    address = raw.sliceArray(2..34)
                ).apply {
                    userFriendly = true
                    urlSafe = false

                    if(raw[0] and 0x80.toByte() != 0.toByte()) {
                        testOnly = true
                        raw[0] = raw[0] and 0x7F.toByte() // not 0x80 = 0x7F; here we clean the test only flag
                    }

                    require((raw[0] == 0x11.toByte()) or (raw[0] == 0x51.toByte())) {"unknown address tag"}

                    bounceable = raw[0] == 0x11.toByte()

                    require((crc(this).toBigInt() == BigInt(raw.sliceArray(35..36)))) {"CRC check failed"}
                }
            }

            fun crc(address: AddrStd): Int =
                crc16(byteArrayOf(tag(), address.workchain_id.toByte()),
                            address.address)

            // Get the tag byte based on set flags
            private fun tag(): Byte = (if(testOnly) 0x80.toByte() else 0.toByte()) or
                    (if(bounceable) 0x11.toByte() else 0x51.toByte())
        }
    }

    @SerialName("addr_var")
    @Serializable
    data class AddrVar(
            val anycast: Anycast?,
            val addr_len: Int,
            val workchain_id: Int,
            @Serializable(HexByteArraySerializer::class)
            val address: ByteArray
    ) : MsgAddressInt {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as AddrVar

            if (anycast != other.anycast) return false
            if (addr_len != other.addr_len) return false
            if (workchain_id != other.workchain_id) return false
            if (!address.contentEquals(other.address)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = anycast?.hashCode() ?: 0
            result = 31 * result + addr_len
            result = 31 * result + workchain_id
            result = 31 * result + address.contentHashCode()
            return result
        }

        override fun toString() = buildString {
            append("MsgAddressInt.AddrVar(anycast=")
            append(anycast)
            append(", addrLen=")
            append(addr_len)
            append(", workchainId=")
            append(workchain_id)
            append(", address=")
            append(hex(address))
            append(")")
        }
    }
}