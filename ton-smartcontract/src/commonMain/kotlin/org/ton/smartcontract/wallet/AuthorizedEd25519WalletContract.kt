package org.ton.smartcontract.wallet

import org.ton.api.pk.PrivateKeyEd25519
import org.ton.cell.Cell
import org.ton.cell.CellBuilder

/**
 * Most often operations on wallets are authorized using a single Ed25519 private key
 */
abstract class AuthorizedEd25519WalletContract(
    private val private_key: PrivateKeyEd25519,
    override val workchain_id: Int = 0,
) : AuthorizedWalletContract {
    /**
     * Get wallet's public key
     */
    fun publicKey() = private_key.publicKey()

    /**
     * Sign arbitrary cell. Use with caution
     */
    fun signCell(data: Cell): ByteArray = private_key.sign(data.hash())

    /**
     * Create signed message payload from a cell builder
     */
    fun createSignedMessageData(builder: CellBuilder.() -> Unit): Cell =
        createSignedMessageData(CellBuilder.createCell { apply(builder) })

    private fun createSignedMessageData(data: Cell): Cell = CellBuilder.createCell {
        storeBytes(signCell(data))
        storeBits(data.bits)
        storeRefs(data.refs)
    }
}
