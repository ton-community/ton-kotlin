package org.ton.kotlin.message.info

import org.ton.kotlin.message.address.MsgAddress

/**
 * Unfinished message info.
 */
public sealed interface RelaxedMsgInfo {
    public val src: MsgAddress?

    public val dest: MsgAddress?
}