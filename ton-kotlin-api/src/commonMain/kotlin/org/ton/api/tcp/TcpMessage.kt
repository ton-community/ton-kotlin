package org.ton.api.tcp

import org.ton.tl.TlCombinator

sealed interface TcpMessage

private object TcpMessageTlCombinator : TlCombinator<TcpMessage>(

)
