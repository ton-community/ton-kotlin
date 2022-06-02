package org.ton.smartcontract.wallet

import org.ton.api.pk.PrivateKeyEd25519
import org.ton.boc.BagOfCells
import org.ton.cell.Cell
import org.ton.crypto.hex
import org.ton.lite.api.LiteApi

class SimpleWalletR3(
    liteApi: LiteApi,
    privateKey: PrivateKeyEd25519,
    workchainId: Int = 0
) : WalletContract(liteApi, privateKey, workchainId) {
    override val name: String = "simpleR3"
    override val code: Cell by lazy {
        BagOfCells(hex("b5ee9c7241010101005f0000baff0020dd2082014c97ba218201339cbab19c71b0ed44d0d31fd70bffe304e0a4f260810200d71820d70b1fed44d0d31fd3ffd15112baf2a122f901541044f910f2a2f80001d31f3120d74a96d307d402fb00ded1a4c8cb1fcbffc9ed54b5b86e42")).first()
    }

    override fun toString(): String = name
}
