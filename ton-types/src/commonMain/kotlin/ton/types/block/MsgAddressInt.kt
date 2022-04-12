package ton.types.block

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import ton.cell.CellReader
import ton.crypto.hex
import ton.types.util.HexByteArraySerializer

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("@type")
@Serializable
sealed interface MsgAddressInt {
    @Serializable
    data class AddrStd(
        val anycast: Anycast?,
        val workchainId: Int,
        @Serializable(HexByteArraySerializer::class)
        val address: ByteArray
    ) : MsgAddressInt {
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
            var result = anycast?.hashCode() ?: 0
            result = 31 * result + workchainId
            result = 31 * result + address.contentHashCode()
            return result
        }

        override fun toString() =
            "MsgAddressInt.AddrStd(anycast=$anycast, workchainId=$workchainId, address=${hex(address)})"
    }

    data class AddrVar(
        val anycast: Anycast?,
        val addrLen: Int,
        val workchainId: Int,
        @Serializable(HexByteArraySerializer::class)
        val address: ByteArray
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as AddrVar

            if (anycast != other.anycast) return false
            if (addrLen != other.addrLen) return false
            if (workchainId != other.workchainId) return false
            if (!address.contentEquals(other.address)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = anycast?.hashCode() ?: 0
            result = 31 * result + addrLen
            result = 31 * result + workchainId
            result = 31 * result + address.contentHashCode()
            return result
        }

        override fun toString() =
            "MsgAddressInt.AddrVar(anycast=$anycast, addrLen=$addrLen, workchainId=$workchainId, address=${hex(address)})"
    }

    companion object {
        fun decode(cellReader: CellReader) {

        }
    }
}