package ton.types.block

import kotlinx.serialization.Serializable
import ton.crypto.hex
import ton.types.util.HexByteArraySerializer

@Serializable
sealed interface AccountState {

    @Serializable
    object AccountUninit : AccountState

    @Serializable
    data class AccountActive(
        val init: StateInit
    ) : AccountState

    @Serializable
    data class AccountFrozen(
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

        override fun toString(): String = "AccountState.AccountFrozen(stateHash=${hex(stateHash)})"
    }
}