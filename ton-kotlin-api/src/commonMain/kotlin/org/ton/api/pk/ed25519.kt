@file:Suppress("NOTHING_TO_INLINE", "OPT_IN_USAGE")

package org.ton.api.pk

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.api.pub.PublicKeyEd25519
import org.ton.bitstring.Bits256
import org.ton.crypto.Decryptor
import org.ton.crypto.DecryptorEd25519
import org.ton.crypto.Ed25519
import org.ton.crypto.SecureRandom
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.TlReader
import org.ton.tl.TlWriter
import kotlin.jvm.JvmStatic
import kotlin.random.Random

public inline fun PrivateKeyEd25519(byteArray: ByteArray): PrivateKeyEd25519 =
    PrivateKeyEd25519.of(byteArray)

public inline fun PrivateKeyEd25519(random: Random = SecureRandom): PrivateKeyEd25519 =
    PrivateKeyEd25519.generate(random)

public interface PrivateKeyEd25519 : PrivateKey {
    public val key: Bits256

    override fun publicKey(): PublicKeyEd25519

    public fun sharedKey(publicKey: PublicKeyEd25519): ByteArray

    public companion object : TlCodec<PrivateKeyEd25519> by PrivateKeyEd25519TlConstructor {
        @JvmStatic
        public fun tlConstructor(): TlConstructor<PrivateKeyEd25519> = PrivateKeyEd25519TlConstructor

        @JvmStatic
        public fun generate(random: Random = SecureRandom): PrivateKeyEd25519 =
            PrivateKeyEd25519Impl(random.nextBytes(32))

        @JvmStatic
        public fun of(byteArray: ByteArray): PrivateKeyEd25519 =
            when (byteArray.size) {
                Ed25519.KEY_SIZE_BYTES -> PrivateKeyEd25519Impl(byteArray.copyOf())
                Ed25519.KEY_SIZE_BYTES + Int.SIZE_BYTES -> decodeBoxed(byteArray)
                else -> throw IllegalArgumentException("Invalid key size: ${byteArray.size}")
            }
    }
}

@Polymorphic
@JsonClassDiscriminator("@type")
@SerialName("pk.ed25519")
@Serializable
private class PrivateKeyEd25519Impl(
    override val key: Bits256
) : PrivateKeyEd25519, Decryptor by DecryptorEd25519(key.toByteArray()) {
    constructor(key: ByteArray) : this(Bits256(key))

    private val _publicKey: PublicKeyEd25519 by lazy(LazyThreadSafetyMode.PUBLICATION) {
        PublicKeyEd25519(Ed25519.publicKey(key.toByteArray()))
    }

    override fun publicKey(): PublicKeyEd25519 = _publicKey

    override fun sharedKey(publicKey: PublicKeyEd25519): ByteArray =
        Ed25519.sharedKey(key.toByteArray(), publicKey.key.toByteArray())

    override fun toString(): String = toAdnlIdShort().toString()
}

private object PrivateKeyEd25519TlConstructor : TlConstructor<PrivateKeyEd25519>(
    schema = "pk.ed25519 key:int256 = PrivateKey"
) {
    override fun encode(writer: TlWriter, value: PrivateKeyEd25519) {
        writer.writeBits256(value.key)
    }

    override fun decode(reader: TlReader): PrivateKeyEd25519 {
        val key = reader.readBits256()
        return PrivateKeyEd25519Impl(key)
    }
}
