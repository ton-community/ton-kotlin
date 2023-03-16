package org.ton.api.pk

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.api.pub.PublicKeyAes
import org.ton.crypto.Decryptor
import org.ton.crypto.DecryptorAes
import org.ton.tl.ByteString
import org.ton.tl.ByteString.Companion.toByteString
import org.ton.tl.TlConstructor
import org.ton.tl.TlReader
import org.ton.tl.TlWriter

@JsonClassDiscriminator("@type")
@SerialName("pk.aes")
@Serializable
public data class PrivateKeyAes(
    val key: ByteString
) : PrivateKey, Decryptor by DecryptorAes(key.toByteArray()) {
    public constructor(key: ByteArray) : this(key.toByteString())

    override fun publicKey(): PublicKeyAes = PublicKeyAes(key)

    override fun toString(): String = toAdnlIdShort().toString()

    public companion object : TlConstructor<PrivateKeyAes>(
        schema = "pk.aes key:int256 = PrivateKey"
    ) {
        override fun encode(output: TlWriter, value: PrivateKeyAes) {
            output.writeRaw(value.key)
        }

        override fun decode(input: TlReader): PrivateKeyAes {
            val key = input.readByteString(32)
            return PrivateKeyAes(key)
        }
    }
}
