package ton.adnl

@JvmInline
value class AdnlAddressJvmImpl(override val bytes: ByteArray) : AdnlAddress

actual fun AdnlAddress(bytes: ByteArray): AdnlAddress = AdnlAddressJvmImpl(bytes)