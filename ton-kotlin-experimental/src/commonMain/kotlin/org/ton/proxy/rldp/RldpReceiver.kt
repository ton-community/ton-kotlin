package org.ton.proxy.rldp

import org.ton.api.rldp.RldpMessagePart

interface RldpReceiver {
    fun receiveRldpMessagePart(message: RldpMessagePart)
}
