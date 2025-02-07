package org.ton.kotlin.message.info

import org.ton.kotlin.currency.Coins
import org.ton.kotlin.currency.CurrencyCollection
import org.ton.kotlin.message.address.IntAddr

/**
 * Internal message info.
 *
 * ```tlb
 * int_msg_info$0 ihr_disabled:Bool bounce:Bool bounced:Bool
 *   src:MsgAddressInt dest:MsgAddressInt
 *   value:CurrencyCollection ihr_fee:Coins fwd_fee:Coins
 *   created_lt:uint64 created_at:uint32 = CommonMsgInfo;
 * ```
 */
public data class IntMsgInfo(
    /**
     * Whether IHR is disabled for the message.
     */
    val ihrDisabled: Boolean = false,

    /**
     * Whether to bounce this message back if the destination transaction fails.
     */
    val bounce: Boolean = false,

    /**
     * Whether this message is a bounced message from some failed transaction.
     */
    val bounced: Boolean = false,

    /**
     * Internal source address.
     */
    override val src: IntAddr,

    /**
     * Internal destination address.
     */
    override val dest: IntAddr,

    /**
     * Attached coin amounts.
     */
    val value: CurrencyCollection = CurrencyCollection.ZERO,

    /**
     * IHR fee.
     *
     * NOTE: currently unused, but can be used to split attached amount.
     */
    val ihrFee: Coins = Coins.ZERO,

    /**
     * Forwarding fee paid for using the routing.
     */
    val fwdFee: Coins = Coins.ZERO,

    /**
     * Logical time when the message was created.
     */
    val createdLt: Long = 0,

    /**
     * Unix timestamp in seconds when the message was created.
     */
    val createdAt: Long = 0
) : MsgInfo