package org.ton.adnl.node

import org.ton.api.adnl.AdnlIdShort
import org.ton.api.pk.PrivateKeyAes
import org.ton.api.pub.PublicKeyAes
import org.ton.crypto.Decryptor
import org.ton.crypto.Encryptor

class AdnlChannel(
    private val decryptorKey: PrivateKeyAes,
    private val encryptorKey: PublicKeyAes
) : Encryptor by encryptorKey, Decryptor by decryptorKey {
    val inputId = AdnlChannelId(decryptorKey)
    val outputId = AdnlChannelId(encryptorKey)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AdnlChannel) return false
        if (outputId != other.outputId) return false
        if (inputId != other.inputId) return false
        return true
    }

    override fun hashCode(): Int {
        var result = outputId.hashCode()
        result = 31 * result + inputId.hashCode()
        return result
    }

    companion object {
        @JvmStatic
        fun of(
            secret: ByteArray,
            localId: AdnlIdShort,
            remoteId: AdnlIdShort
        ): AdnlChannel {
            val compare = localId.compareTo(remoteId)
            val decryptorKey: PrivateKeyAes
            val encryptorKey: PublicKeyAes
            if (compare == 0) {
                decryptorKey = PrivateKeyAes(secret)
                encryptorKey = PublicKeyAes(secret)
            } else {
                val revSecret = secret.reversedArray()
                if (compare < 0) {
                    decryptorKey = PrivateKeyAes(secret)
                    encryptorKey = PublicKeyAes(revSecret)
                } else {
                    decryptorKey = PrivateKeyAes(revSecret)
                    encryptorKey = PublicKeyAes(secret)
                }
            }
            return AdnlChannel(decryptorKey, encryptorKey)
        }
    }
}
