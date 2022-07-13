package org.ton.adnl

import io.ktor.utils.io.core.*
import org.ton.adnl.aes.AdnlAesCipher
import org.ton.api.adnl.AdnlIdShort
import org.ton.api.pk.PrivateKey
import org.ton.api.pk.PrivateKeyAes
import org.ton.api.pub.PublicKey
import org.ton.crypto.encodeHex
import org.ton.crypto.sha256

class AdnlHandshake(
    val payload: ByteArray,
    val local: PrivateKey,
    val other: PublicKey
) {
    companion object {
        fun encode(output: Output, handshake: AdnlHandshake) {
            val checksum = sha256(handshake.payload)
            val preimage = handshake.payload.copyOf(96)
            val localAes = PrivateKeyAes(handshake.local)

            handshake.other.toAdnlIdShort().id.copyInto(preimage, destinationOffset = 0, startIndex = 0, endIndex = 32)
            localAes.publicKey().key.copyInto(preimage, destinationOffset = 32, startIndex = 0, endIndex = 32)
            checksum.copyInto(preimage, destinationOffset = 64, startIndex = 0, endIndex = 32)

            val sharedSecret = localAes.sharedSecret(handshake.other)
            val cipher = try {
                AdnlAesCipher.secure(sharedSecret, checksum)
            } finally {
                sharedSecret.fill(0)
            }

            output.writeFully(preimage)
            output.writeFully(cipher.encrypt(handshake.payload))
            println("encrypt payload: ${handshake.payload.encodeHex()}")
        }

        fun decode(input: Input, keys: Map<AdnlIdShort, PublicKey>, length: Int = 0) {
            check(input.remaining >= (96 + length)) { "Bad handshake packet length: ${input.remaining}" }
            val id = AdnlIdShort(input.readBytes(32))
            TODO()
        }
    }
}