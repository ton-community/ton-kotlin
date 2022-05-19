@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package org.ton.block

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.bigint.BigInt
import org.ton.bigint.toBigInt
import org.ton.crypto.*
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

        fun toString(
            userFriendly: Boolean = true,
            urlSafe: Boolean = true,
            testOnly: Boolean = false,
            bounceable: Boolean = true
        ): String {
            if (userFriendly) {
                val raw = byteArrayOf(tag(testOnly, bounceable), workchain_id.toByte()) +
                        address + crc(this, testOnly, bounceable).toShort().toBigInt().toByteArray()
                if (urlSafe) {
                    return base64url(raw)
                } else {
                    return base64(raw)
                }
            } else {
                return workchain_id.toString() + ":" + hex(address)
            }
        }

        companion object {
            @JvmStatic
            fun parse(address: String): AddrStd {
                if (address.contains(':')) {
                    return parseRaw(address)
                } else {
                    return parseUserFriendly(address)
                }
            }

            @JvmStatic
            fun parseRaw(address: String): AddrStd {
                require(address.contains(':'))
                // 32 bytes, each represented as 2 characters
                require(address.substringAfter(':').length == 32 * 2)
                return AddrStd(
                    anycast = null,
                    // toByte() to make sure it fits into 8 bits
                    workchain_id = address.substringBefore(':').toByte().toInt(),
                    address = hex(address.substringAfter(':'))
                )
            }

            @JvmStatic
            fun parseUserFriendly(address: String): AddrStd {
                var raw: ByteArray
                try {
                    raw = base64url(address)
                } catch (E: Exception) {
                    raw = base64(address)
                }

                require(raw.size == 36)
                return AddrStd(
                    anycast = null,
                    workchain_id = raw[1].toInt(),
                    address = raw.sliceArray(2..33)
                ).apply {
                    val testOnly = raw[0] and 0x80.toByte() != 0.toByte()
                    if (testOnly) {
                        // not 0x80 = 0x7F; here we clean the test only flag
                        raw[0] = raw[0] and 0x7F.toByte()
                    }

                    require((raw[0] == 0x11.toByte()) or (raw[0] == 0x51.toByte())) { "unknown address tag" }

                    val bounceable = raw[0] == 0x11.toByte()
                    require(
                        (crc(
                            this,
                            testOnly,
                            bounceable
                        ).toBigInt() == BigInt(raw.sliceArray(34..35)))
                    ) { "CRC check failed" }
                }
            }

            @JvmStatic
            private fun crc(address: AddrStd, testOnly: Boolean, bounceable: Boolean): Int =
                crc16(
                    byteArrayOf(tag(testOnly, bounceable), address.workchain_id.toByte()),
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