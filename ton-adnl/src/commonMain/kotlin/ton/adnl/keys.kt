package ton.adnl

import ton.crypto.Ed25519
import ton.crypto.X25519
import ton.crypto.hex
import ton.crypto.sha256

@JvmInline
value class AdnlPrivateKey(
    val value: ByteArray,
) {
    fun public(): AdnlPublicKey =
        AdnlPublicKey(X25519.convertToEd25519(X25519.publicKey(value)))

    fun sharedKey(publicKey: AdnlPublicKey): AdnlSharedKey =
        AdnlSharedKey(X25519.sharedKey(value, Ed25519.convertToX25519(publicKey.value)))

    override fun toString(): String = hex(value)

    companion object {
        fun random(): AdnlPrivateKey = AdnlPrivateKey(X25519.privateKey())
    }
}

@JvmInline
value class AdnlPublicKey(
    val value: ByteArray,
) {
    fun address(): AdnlAddress = AdnlAddress(sha256(ED25519_MAGIC, value))

    override fun toString(): String = hex(value)

    companion object {
        val ED25519_MAGIC = byteArrayOf(0xc6.toByte(), 0xb4.toByte(), 0x13, 0x48)
    }
}

@JvmInline
value class AdnlSharedKey(
    val value: ByteArray,
) {
    override fun toString(): String = hex(value)
}
