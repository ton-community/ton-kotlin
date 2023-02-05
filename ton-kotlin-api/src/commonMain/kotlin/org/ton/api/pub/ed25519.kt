@file:Suppress("NOTHING_TO_INLINE")

package org.ton.api.pub

import io.ktor.utils.io.core.*
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.adnl.AdnlIdShort
import org.ton.api.pk.PrivateKeyEd25519
import org.ton.bitstring.Bits256
import org.ton.crypto.*
import org.ton.tl.*
import kotlin.jvm.JvmStatic

public inline fun PublicKeyEd25519(privateKey: PrivateKeyEd25519): PublicKeyEd25519 = PublicKeyEd25519.of(privateKey)

@Serializable
@SerialName("pub.ed25519")
@Polymorphic
public data class PublicKeyEd25519(
    val key: Bits256
) : PublicKey, Encryptor by EncryptorEd25519(key.toByteArray()) {
    public constructor(key: ByteArray) : this(Bits256(key))

    private val _adnlIdShort: AdnlIdShort by lazy(LazyThreadSafetyMode.PUBLICATION) {
        AdnlIdShort(PublicKeyEd25519.hash(this))
    }

    override fun toAdnlIdShort(): AdnlIdShort = _adnlIdShort

    public companion object : TlCodec<PublicKeyEd25519> by PublicKeyEd25519TlConstructor {
        @JvmStatic
        public fun tlConstructor(): TlConstructor<PublicKeyEd25519> = PublicKeyEd25519TlConstructor

        @JvmStatic
        public fun of(privateKey: PrivateKeyEd25519): PublicKeyEd25519 =
            PublicKeyEd25519(Ed25519.publicKey(privateKey.key.toByteArray()))
    }
}

private object PublicKeyEd25519TlConstructor : TlConstructor<PublicKeyEd25519>(
    schema = "pub.ed25519 key:int256 = PublicKey",
) {
    override fun encode(writer: TlWriter, value: PublicKeyEd25519) {
        writer.writeBits256(value.key)
    }

    override fun decode(reader: TlReader): PublicKeyEd25519 {
        val key = reader.readBits256()
        return PublicKeyEd25519(key)
    }
}
