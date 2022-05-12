package org.ton.api.dht

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.tl.TlEnum

@Serializable
enum class DhtUpdateRule {
    @SerialName("dht.updateRule.signature")
    SIGNATURE,

    @SerialName("dht.updateRule.anybody")
    ANYBODY,

    @SerialName("dht.updateRule.overlayNodes")
    OVERLAY_NODES;

    companion object : TlEnum<DhtUpdateRule>(
            DhtUpdateRule::class,
            SIGNATURE to "dht.updateRule.signature = dht.UpdateRule",
            ANYBODY to "dht.updateRule.anybody = dht.UpdateRule",
            OVERLAY_NODES to "dht.updateRule.overlayNodes = dht.UpdateRule"
    )
}