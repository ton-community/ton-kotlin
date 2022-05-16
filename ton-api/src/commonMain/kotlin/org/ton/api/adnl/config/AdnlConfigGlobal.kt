package org.ton.api.adnl.config

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.adnl.AdnlNodes
import org.ton.tl.TlConstructor
import org.ton.tl.readTl
import org.ton.tl.writeTl

@SerialName("adnl.config.global")
@Serializable
data class AdnlConfigGlobal(
    @SerialName("static_nodes")
    val staticNodes: AdnlNodes
) {
    companion object : TlConstructor<AdnlConfigGlobal>(
        type = AdnlConfigGlobal::class,
        schema = "adnl.config.global static_nodes:adnl.nodes = adnl.config.Global"
    ) {
        override fun encode(output: Output, value: AdnlConfigGlobal) {
            output.writeTl(value.staticNodes, AdnlNodes)
        }

        override fun decode(input: Input): AdnlConfigGlobal {
            val staticNodes = input.readTl(AdnlNodes)
            return AdnlConfigGlobal(staticNodes)
        }
    }
}