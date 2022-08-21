@file:Suppress("OPT_IN_USAGE")

package org.ton.api.adnl.proxy

import io.ktor.utils.io.core.*
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tl.TlCombinator

@JsonClassDiscriminator("@type")
sealed interface AdnlProxy {
    val id: ByteArray

    companion object : TlCombinator<AdnlProxy>(
        AdnlProxyNone,
        AdnlProxyFast
    )
}
