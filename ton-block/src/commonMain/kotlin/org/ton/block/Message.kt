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
        init: Pair<StateInit?, StateInit?>?,
        body: Pair<X?, X?>
    ) : this(info, init?.toEither().toMaybe(), body.toEither())

    constructor(
        info: CommonMsgInfo,
        init: StateInit?,
        body: X?,
        storeInitInRef: Boolean = true,
        storeBodyInRef: Boolean = true
    ) : this(
        info = info,
        init = init?.let {
            if (storeInitInRef) null to init else init to null
        },
        body = if (storeBodyInRef) null to body else body to null
    )
}
