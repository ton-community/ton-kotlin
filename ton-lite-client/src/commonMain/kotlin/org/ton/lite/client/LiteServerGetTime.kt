package org.ton.lite.client

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.adnl.TLCodec

@Serializable
object LiteServerGetTime : TLCodec<LiteServerGetTime> {
    override val id: Int = 380459572

    override fun decode(input: Input): LiteServerGetTime = LiteServerGetTime

    override fun encode(output: Output, message: LiteServerGetTime) {
    }
}