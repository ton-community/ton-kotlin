package org.ton.smartcontract.wallet

import org.ton.api.pk.PrivateKeyEd25519
import org.ton.block.CommonMsgInfo
import org.ton.block.MsgAddressInt
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.smartcontract.SmartContract

abstract class WalletContract(
    val privateKey: PrivateKeyEd25519,
    override val workchainId: Int = 0
) : SmartContract {
    val publicKey by lazy {
        privateKey.publicKey()
    }

    override fun createData(): Cell = CellBuilder.createCell {
        storeUInt(0, 32) // seqno
        storeBytes(privateKey.key)
    }

    fun createSigningMessage(seqno: Int = 0): Cell = CellBuilder.createCell {
        storeUInt(seqno, 32)
    }

    fun createInitExternalMessage(
        dest: MsgAddressInt.AddrStd
    ) {
        val stateInit = createStateInit()
        val signingMessage = createSigningMessage()
        val signature = privateKey.sign(signingMessage.hash())

        val extInMsgInfo = CommonMsgInfo.ExtInMsgInfo(dest)

    }
}
