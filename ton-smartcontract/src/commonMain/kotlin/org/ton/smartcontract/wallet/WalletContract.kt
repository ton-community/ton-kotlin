package org.ton.smartcontract.wallet

import org.ton.api.pk.PrivateKeyEd25519
import org.ton.bitstring.BitString
import org.ton.block.CommonMsgInfo
import org.ton.block.Message
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

    fun createInitMessage(): Message<BitString> {
        val stateInit = createStateInit()
        val dest = address(stateInit)
        val signingMessage = createSigningMessage()
        val signature = privateKey.sign(signingMessage.hash())
        val body = CellBuilder.createCell {
            storeBytes(signature)
            storeBits(signingMessage.bits)
        }.bits
        val info = CommonMsgInfo.ExtInMsgInfo(dest)
        return Message(
            info,
            stateInit to null,
            body to null
        )
    }
}
