package org.ton.adnl.node

import io.ktor.util.pipeline.*

class AdnlRequestPipeline(
    override val developmentMode: Boolean = false
) : Pipeline<Any, AdnlRequestBuilder>(SEND) {
    companion object Phases {
        val SEND = PipelinePhase("Send")
    }
}

class AdnlSendPipeline(
    override val developmentMode: Boolean = false
) : Pipeline<Any, AdnlRequestBuilder>(ENGINE, RECEIVE) {
    companion object Phases {
        val ENGINE = PipelinePhase("Engine")
        val RECEIVE = PipelinePhase("Receive")
    }
}

