package org.ton.api.pk

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.pub.PublicKeyEd25519
import org.ton.crypto.Decryptor
import org.ton.crypto.DecryptorEd25519
import org.ton.crypto.Ed25519
import org.ton.crypto.SecureRandom
import org.ton.tl.*
import org.ton.tl.ByteString.Companion.toByteString
import kotlin.jvm.JvmStatic
import kotlin.random.Random

public inline fun PrivateKeyEd25519(random: Random = SecureRandom): PrivateKeyEd25519 =
    PrivateKeyEd25519.generate(random)

@Serializable
@SerialName("pk.ed25519")
public data class PrivateKeyEd25519(
    public val key: ByteString
) : PrivateKey, Decryptor {
    public constructor(key: ByteArray) : this(key.toByteString())

    init {
        require(key.size == 32) { "key must be 32 byte long" }
    }

    private val _publicKey: PublicKeyEd25519 by lazy(LazyThreadSafetyMode.PUBLICATION) {
        PublicKeyEd25519(Ed25519.publicKey(key.toByteArray()).asByteString())
    }
    private val _decryptor by lazy(LazyThreadSafetyMode.PUBLICATION) {
        DecryptorEd25519(key.toByteArray())
    }

    override fun publicKey(): PublicKeyEd25519 = _publicKey

    public fun sharedKey(publicKey: PublicKeyEd25519): ByteArray =
        Ed25519.sharedKey(key.toByteArray(), publicKey.key.toByteArray())

    public companion object : TlConstructor<PrivateKeyEd25519>(
        schema = "pk.ed25519 key:int256 = PrivateKey"
    ) {
        @JvmStatic
        public fun tlConstructor(): TlConstructor<PrivateKeyEd25519> = this

        @JvmStatic
        public fun generate(random: Random = SecureRandom): PrivateKeyEd25519 =
            PrivateKeyEd25519(random.nextBytes(32).asByteString())

        @JvmStatic
        public fun of(byteArray: ByteArray): PrivateKeyEd25519 =
            when (byteArray.size) {
                Ed25519.KEY_SIZE_BYTES -> PrivateKeyEd25519(byteArray.toByteString())
                Ed25519.KEY_SIZE_BYTES + Int.SIZE_BYTES -> decodeBoxed(byteArray)
                else -> throw IllegalArgumentException("Invalid key size: ${byteArray.size}")
            }

        public override fun encode(writer: TlWriter, value: PrivateKeyEd25519) {
            writer.writeRaw(value.key)
        }

        public override fun decode(reader: TlReader): PrivateKeyEd25519 {
            val key = reader.readByteString(32)
            return PrivateKeyEd25519(key)
        }
    }

    override fun decrypt(data: ByteArray): ByteArray =
        _decryptor.decrypt(data)

    override fun sign(message: ByteArray): ByteArray =
        _decryptor.sign(message)
}
