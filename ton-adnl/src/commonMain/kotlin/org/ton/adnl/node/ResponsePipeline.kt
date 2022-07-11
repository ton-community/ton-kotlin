package org.ton.adnl.node

import io.ktor.util.pipeline.*

class AdnlResponsePipeline(
    override val developmentMode: Boolean = false
) : Pipeline<ByteArray, AdnlQuery>(
    RECEIVE
) {
    companion object Phases {
        val RECEIVE = PipelinePhase("Receive")
    }
}

class AdnlReceivePipeline(
    override val developmentMode: Boolean = false
) : Pipeline<AdnlResponse, Unit>()