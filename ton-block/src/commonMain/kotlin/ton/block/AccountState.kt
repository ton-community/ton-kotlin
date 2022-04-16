@file:Suppress("OPT_IN_USAGE")

package ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import ton.crypto.HexByteArraySerializer
import ton.crypto.hex

@JsonClassDiscriminator("@type")
@Serializable
sealed interface AccountState {
    @SerialName("account_uninit")
    @Serializable
    object AccountUninit : AccountState

    @SerialName("account_active")
    @Serializable
    data class AccountActive(
        @SerialName("_")
        val init: StateInit
    ) : AccountState

    @SerialName("account_frozen")
    @Serializable
    data class AccountFrozen(
        @SerialName("state_hash")
        @Serializable(HexByteArraySerializer::class)
        val stateHash: ByteArray
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as AccountFrozen

            if (!stateHash.contentEquals(other.stateHash)) return false

            return true
        }

        override fun hashCode(): Int {
            return stateHash.contentHashCode()
        }

        override fun toString(): String = buildString {
            append("AccountFrozen(stateHash=")
            append(hex(stateHash))
            append(")")
        }
    }
}