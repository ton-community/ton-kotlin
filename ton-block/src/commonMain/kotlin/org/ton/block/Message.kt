package org.ton.block

import kotlinx.serialization.Serializable

@Serializable
data class Message<X : Any>(
    val info: CommonMsgInfo,
    val init: Maybe<Either<StateInit, StateInit>>,
    val body: Either<X, X>
) {
    constructor(
        info: CommonMsgInfo,
        init: Pair<StateInit?, StateInit?>? = null,
        body: Pair<X?, X?>
    ) : this(info, init?.toEither().toMaybe(), body.toEither())
}
