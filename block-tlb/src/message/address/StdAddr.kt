package org.ton.kotlin.message.address

import kotlinx.io.bytestring.ByteString
import org.ton.crypto.crc16
import org.ton.kotlin.bitstring.BitString
import org.ton.kotlin.bitstring.toBitString
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.io.encoding.Base64
import kotlin.jvm.JvmStatic

/**
 * Standard internal address.
 *
 * ```tlb
 * addr_std$10 anycast:(Maybe Anycast)
 *    workchain_id:int8 address:bits256  = MsgAddressInt;
 * ```
 */
public data class StdAddr(
    val anycast: Anycast?,
    override val workchain: Int,
    override val address: BitString
) : IntAddr {
    init {
        require(workchain in -128..128) { "workchainId must be in range -128..128: $workchain" }
        require(address.size == 256) { "expected address.size == 256, actual: ${address.size}" }
    }

    public constructor(anycast: Anycast?, workchain: Int, address: ByteString) : this(
        anycast,
        workchain,
        BitString(address)
    )

    public constructor(anycast: Anycast?, workchain: Int, address: ByteArray) : this(
        anycast,
        workchain,
        BitString(address)
    )

    public constructor(workchain: Int, address: BitString) : this(null, workchain, address)
    public constructor(workchain: Int, address: ByteString) : this(null, workchain, BitString(address))
    public constructor(workchain: Int, address: ByteArray) : this(null, workchain, BitString(address))

    override fun toAddrStd(): StdAddr = this

    override fun rewriteAnycast(): StdAddr = StdAddr(
        workchain,
        anycast?.rewrite(address) ?: address,
    )

    public fun toString(
        userFriendly: Boolean = true,
        urlSafe: Boolean = true,
        testOnly: Boolean = false,
        bounceable: Boolean = true
    ): String = toString(this, userFriendly, urlSafe, testOnly, bounceable)

    public fun toBase64(
        testnet: Boolean = false,
        bounceable: Boolean = true,
    ): String = toString(this, true, true, testnet, bounceable)

    public companion object {
        @JvmStatic
        public fun toString(
            address: StdAddr,
            userFriendly: Boolean = true,
            urlSafe: Boolean = true,
            testOnly: Boolean = false,
            bounceable: Boolean = true
        ): String {
            return if (userFriendly) {
                val tag = tag(testOnly, bounceable)
                val workchain = address.workchain
                val rawAddress = address.address.toByteArray()
                val checksum = checksum(tag, workchain, rawAddress)

                val data = ByteArray(36)
                data[0] = tag
                data[1] = workchain.toByte()
                rawAddress.copyInto(data, 2)
                data[32 + 2] = (checksum ushr 8).toByte()
                data[32 + 2 + 1] = (checksum).toByte()

                if (urlSafe) {
                    Base64.UrlSafe.encode(data)
                } else {
                    Base64.encode(data)
                }
            } else {
                "${address.workchain}:${address.address.toHexString()}"
            }
        }

        @JvmStatic
        public fun parse(address: String): StdAddr = try {
            if (address.contains(':')) {
                parseRaw(address)
            } else {
                parseUserFriendly(address)
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("Can't parse address: $address", e)
        }

        @JvmStatic
        public fun parseRaw(address: String): StdAddr {
            require(address.contains(':'))
            // 32 bytes, each represented as 2 characters
            require(address.substringAfter(':').length == 32 * 2)
            return StdAddr(
                anycast = null,
                // toByte() to make sure it fits into 8 bits
                workchain = address.substringBefore(':').toByte().toInt(),
                address = BitString(address.substringAfter(':'))
            )
        }

        @JvmStatic
        public fun parseUserFriendly(address: String): StdAddr {
            val addressBytes = ByteArray(36)

            try {
                Base64.UrlSafe.decode(address).copyInto(addressBytes)
            } catch (e: Exception) {
                try {
                    Base64.decode(address).copyInto(addressBytes)
                } catch (e: Exception) {
                    throw IllegalArgumentException("Can't parse address: $address", e)
                }
            }
            val tag = addressBytes[0]
            val cleanTestOnly = tag and 0x7F.toByte()
            check((cleanTestOnly == 0x11.toByte()) or (cleanTestOnly == 0x51.toByte())) {
                "unknown address tag"
            }
            var workchainId = addressBytes[1].toInt()
            var rawAddress = addressBytes.copyOfRange(fromIndex = 2, toIndex = 2 + 32)
            var expectedChecksum =
                ((addressBytes[2 + 32].toInt() and 0xFF) shl 8) or (addressBytes[2 + 32 + 1].toInt() and 0xFF)

            val actualChecksum = checksum(tag, workchainId, rawAddress)
            check(expectedChecksum == actualChecksum) {
                "CRC check failed"
            }

            return StdAddr(
                workchain = workchainId,
                address = rawAddress.toBitString()
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

public inline fun StdAddr(address: String): StdAddr = StdAddr.parse(address)
