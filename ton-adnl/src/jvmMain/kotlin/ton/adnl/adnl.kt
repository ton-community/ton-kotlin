package ton.adnl

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.util.*
import java.security.SecureRandom

class AdnlClient(
    val publicKey: ByteArray,
    val host: String,
    val port: Int,
) {
    lateinit var socket: Socket

    suspend fun connect() {
        socket = aSocket(SelectorManager())
            .tcp()
            .connect(host, port)

        val localSecret = ByteArray(256)
        SecureRandom.getInstanceStrong().nextBytes(localSecret)


    }
}

/**
 * Public key can be provided using raw slice
 */
@JvmInline
value class AdnlPublicKey(val value: ByteArray = ByteArray(32)) {
    suspend fun address(): AdnlAddress {
        val digest = Digest("SHA-256")
        digest += ED25519_MAGIC
        digest += value
        return AdnlAddress(digest.build())
    }

    companion object {
        val ED25519_MAGIC = byteArrayOf(0xc6.toByte(), 0xb4.toByte(), 0x13, 0x48)
    }
}

/**
 * Trait which must be implemented to perform key agreement inside [`AdnlHandshake`]
 */
class AdnlPrivateKey {
    /**
     * Get public key corresponding to this private
     */
    fun public(): AdnlPublicKey {
        TODO()
    }
}

/**
 * Wrapper struct to hold ADNL address, which is a hash of public key
 */
@JvmInline
value class AdnlAddress(val bytes: ByteArray = ByteArray(32))

/**
 * Wrapper struct to hold the secret, result of ECDH between peers
 */
@JvmInline
value class AdnlSecret(val bytes: ByteArray = ByteArray(32))

