package org.ton.lite.client

fun LiteClientConfig(block: LiteClientConfig.() -> Unit) = LiteClientConfig().apply(block)

data class LiteClientConfig(
    var ipv4: Int = 0,
    var port: Int = 0,
    var publicKey: ByteArray = ByteArray(0),
    var blockIdCacheSize: Int = 100,
) {
    fun ipv4(string: String) {
        this.ipv4 = org.ton.adnl.ipv4(string)
    }
}
