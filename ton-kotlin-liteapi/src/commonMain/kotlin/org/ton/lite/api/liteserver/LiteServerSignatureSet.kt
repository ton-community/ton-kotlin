package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.readVectorTl
import org.ton.tl.constructors.writeIntTl
import org.ton.tl.constructors.writeVectorTl

@Serializable
data class LiteServerSignatureSet(
    val validator_set_hash: Int,
    val catchain_seqno: Int,
    val signatures: List<LiteServerSignature>
) : Iterable<LiteServerSignature> by signatures {
    companion object : TlCodec<LiteServerSignatureSet> by LiteServerSignatureSetTlConstructor
}

private object LiteServerSignatureSetTlConstructor : TlConstructor<LiteServerSignatureSet>(
    type = LiteServerSignatureSet::class,
    schema = "liteServer.signatureSet validator_set_hash:int catchain_seqno:int signatures:(vector liteServer.signature) = liteServer.SignatureSet"
) {
    override fun decode(input: Input): LiteServerSignatureSet {
        val validatorSetHash = input.readIntTl()
        val catchainSeqno = input.readIntTl()
        val signatures = input.readVectorTl(LiteServerSignature)
        return LiteServerSignatureSet(validatorSetHash, catchainSeqno, signatures)
    }

    override fun encode(output: Output, value: LiteServerSignatureSet) {
        output.writeIntTl(value.validator_set_hash)
        output.writeIntTl(value.catchain_seqno)
        output.writeVectorTl(value.signatures, LiteServerSignature)
    }
}