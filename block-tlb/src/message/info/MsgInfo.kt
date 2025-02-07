@file:Suppress("OPT_IN_USAGE")

package org.ton.kotlin.message.info

import org.ton.kotlin.message.address.MsgAddress

/**
 * Message info.
 */
public sealed interface MsgInfo {
    /**
     * Optional source address.
     */
    public val src: MsgAddress?

    /**
     * Optional destination address.
     */
    public val dest: MsgAddress?
}
