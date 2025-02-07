package org.ton.kotlin.account

import kotlinx.io.bytestring.ByteString

/**
 * State of an existing account.
 */
public sealed interface AccountState {
    /**
     * Account status.
     */
    public val status: AccountStatus

    /**
     * Account exists but has not yet been deployed.
     */
    public object Uninit : AccountState {
        override val status: AccountStatus get() = AccountStatus.Uninit
    }

    /**
     * Account exists and has been deployed.
     */
    public data class Active(
        public val state: StateInit
    ) : AccountState {
        override val status: AccountStatus get() = AccountStatus.Active
    }

    /**
     * Account exists but has been frozen. Contains a hash of the last known [StateInit].
     */
    public data class Frozen(
        public val stateHash: ByteString
    ) : AccountState {
        override val status: AccountStatus get() = AccountStatus.Frozen
    }
}

/*

    public object Tlb : TlbCodec<AccountState> {
        override fun storeTlb(cellBuilder: CellBuilder, value: AccountState) {
            when (value) {
                is Active -> {
                    cellBuilder.storeBoolean(true)
                    cellBuilder.storeTlb(StateInit.Tlb, value.state)
                }

                is Frozen -> {
                    cellBuilder.storeUInt(0b01, 2)
                    cellBuilder.storeByteString(value.stateHash)
                }

                Uninit -> {
                    cellBuilder.storeUInt(0b00, 2)
                }
            }
        }

        override fun loadTlb(cellSlice: CellSlice): AccountState {
            val tag = cellSlice.preloadUInt(2).toInt()
            when (tag) {
                0b00 -> { // account_uninit$00
                    cellSlice.skipBits(2)
                    return Uninit
                }

                0b01 -> { // account_frozen$01
                    cellSlice.skipBits(2)
                    val hash = cellSlice.loadByteString(256)
                    return Frozen(hash)
                }

                0b10, 0b11 -> { // account_active$1
                    cellSlice.skipBits(1)
                    val state = cellSlice.loadTlb(StateInit.Tlb)
                    return Active(state)
                }

                else -> throw IllegalStateException("Invalid tag $tag")
            }
        }
 */