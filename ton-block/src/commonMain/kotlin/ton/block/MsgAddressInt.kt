package ton.block

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import ton.crypto.HexByteArraySerializer
import ton.crypto.hex

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