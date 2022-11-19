package org.ton.experimental

import org.ton.proxy.CONFIG_GLOBAL
import org.ton.proxy.ProxyClient

fun main() {
    ProxyClient(CONFIG_GLOBAL).start()
}
