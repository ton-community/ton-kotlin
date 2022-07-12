package org.ton.smartcontract.wallet

import org.ton.cell.Cell
import org.ton.smartcontract.SmartContract

/**
 * Base interface for all wallet contracts, any externally-controlled contract whose main purpose is sending and receiving funds.
 */
interface Wallet : SmartContract {
    override fun createDataInit(): Cell {
        throw Exception("cannot create data cell")
    }
}
