package org.ton.smartcontract.wallet

import org.ton.api.pk.PrivateKeyEd25519
import org.ton.boc.BagOfCells
import org.ton.cell.Cell
import org.ton.crypto.hex
import org.ton.lite.api.LiteApi

class WalletV2R2(
    liteApi: LiteApi,
    privateKey: PrivateKeyEd25519,
    workchainId: Int = 0,
    timeout: Long = 60
) : AbstractWalletV2(liteApi, privateKey, workchainId, timeout) {
    override val name: String = "v2R2"
    override val code: Cell by lazy {
        BagOfCells(hex("B5EE9C724101010100630000C2FF0020DD2082014C97BA218201339CBAB19C71B0ED44D0D31FD70BFFE304E0A4F2608308D71820D31FD31F01F823BBF263ED44D0D31FD3FFD15131BAF2A103F901541042F910F2A2F800029320D74A96D307D402FB00E8D1A4C8CB1FCBFFC9ED54044CD7A1")).first()
    }
}
