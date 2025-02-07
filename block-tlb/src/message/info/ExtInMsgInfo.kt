package org.ton.kotlin.message.info

import org.ton.kotlin.currency.Coins
import org.ton.kotlin.message.address.ExtAddr
import org.ton.kotlin.message.address.IntAddr


/**
 * External incoming message info.
 *
 * ```tlb
 * ext_in_msg_info$10 src:MsgAddressExt dest:MsgAddressInt
 *   import_fee:Coins = CommonMsgInfo;
 * ```
 */
public data class ExtInMsgInfo(
    /**
     * Optional external source address.
     */
    override val src: ExtAddr? = null,

    /**
     * Internal destination address.
     */
    override val dest: IntAddr,

    /**
     * External message import fee.
     *
     * NOTE: currently unused and reserved for future use.
     */
    val importFee: Coins = Coins.ZERO,
) : MsgInfo