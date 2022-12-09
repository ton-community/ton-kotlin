package org.ton.lite.api.liteserver.functions

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.writeIntTl

@Serializable
data class LiteServerWaitMasterchainSeqno(
    val seqno: Int,
    val timeout_ms: Int
) {
    companion object : TlCodec<LiteServerWaitMasterchainSeqno> by LiteServerWaitMasterchainSeqnoTlConstructor
}

private object LiteServerWaitMasterchainSeqnoTlConstructor : TlConstructor<LiteServerWaitMasterchainSeqno>(
    schema = "liteServer.waitMasterchainSeqno seqno:int timeout_ms:int = Object"
) {
    override fun decode(input: Input): LiteServerWaitMasterchainSeqno {
        val seqno = input.readIntTl()
        val timeoutMs = input.readIntTl()
        return LiteServerWaitMasterchainSeqno(seqno, timeoutMs)
    }

    override fun encode(output: Output, value: LiteServerWaitMasterchainSeqno) {
        output.writeIntTl(value.seqno)
        output.writeIntTl(value.timeout_ms)
    }
}
