package ton.adnl

import io.ktor.util.*
import ton.crypto.Crypto

@JvmInline
value class AdnlPublicKeyJvmImpl(override val bytes: ByteArray) : AdnlPublicKey {
    override suspend fun address(): AdnlAddress {
        val digest = Digest("SHA-256")
        digest += ED25519_MAGIC
        digest += bytes
        return AdnlAddress(digest.build())
    }

    companion object {
        val ED25519_MAGIC = byteArrayOf(0xc6.toByte(), 0xb4.toByte(), 0x13, 0x48)
    }
}

actual fun AdnlPublicKey(bytes: ByteArray): AdnlPublicKey = AdnlPublicKeyJvmImpl(bytes)

@JvmInline
value class AdnlPrivateKeyJvmImpl(override val bytes: ByteArray) : AdnlPrivateKey {

    override suspend fun public(): AdnlPublicKey = AdnlPublicKey(
        Crypto.generateKeyPair(bytes).publicKey
    )

    /**
     * Perform key agreement protocol (usually x25519) between our private key and their public
     */
    override suspend fun sharedKey(publicKey: AdnlPublicKey): AdnlSharedKey =
        AdnlSharedKeyJvmImpl(Crypto.sharedKey(bytes, publicKey.bytes))
}

actual fun AdnlPrivateKey(bytes: ByteArray): AdnlPrivateKey = AdnlPrivateKeyJvmImpl(bytes)

@JvmInline
value class AdnlSharedKeyJvmImpl(override val bytes: ByteArray) : AdnlSharedKey