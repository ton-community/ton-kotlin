package org.ton.adnl.node

import io.ktor.events.*

val AdnlRequestCreated = EventDefinition<AdnlRequestBuilder>()

val AdnlRequestIsReadyForSending = EventDefinition<AdnlRequestBuilder>()

val AdnlResponseReceived = EventDefinition<AdnlResponse>()

class AdnlResponseReceiveFail(
    val response: AdnlResponse,
    val cause: Throwable
)

val AdnlResponseReceiveFailed = EventDefinition<AdnlResponseReceiveFail>()

val AdnlResponseCancelled = EventDefinition<AdnlResponse>()