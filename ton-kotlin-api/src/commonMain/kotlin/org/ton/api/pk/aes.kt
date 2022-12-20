package org.ton.api.pk

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.api.pub.PublicKeyAes
import org.ton.bitstring.BitString
import org.ton.bitstring.toBitString
import org.ton.crypto.Decryptor
import org.ton.crypto.DecryptorAes
import org.ton.tl.Bits256
import org.ton.tl.TlConstructor
import org.ton.tl.TlReader
import org.ton.tl.TlWriter

@JsonClassDiscriminator("@type")
@SerialName("pk.aes")
@Serializable
public data class PrivateKeyAes(
    val key: Bits256
) : PrivateKey, Decryptor by DecryptorAes(key.toByteArray()) {
    public constructor(key: ByteArray) : this(Bits256(key))

    override fun publicKey(): PublicKeyAes = PublicKeyAes(key)

    override fun toString(): String = toAdnlIdShort().toString()

    public companion object : TlConstructor<PrivateKeyAes>(
        schema = "pk.aes key:int256 = PrivateKey"
    ) {
        override fun encode(output: TlWriter, value: PrivateKeyAes) {
            output.writeBits256(value.key)
        }

        override fun decode(input: TlReader): PrivateKeyAes {
            val key = input.readBits256()
            return PrivateKeyAes(key)
        }
    }
}
