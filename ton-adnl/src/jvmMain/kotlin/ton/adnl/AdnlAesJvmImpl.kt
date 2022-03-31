package ton.adnl

import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AdnlAesJvmImpl(
    override val key: ByteArray,
    override val nonce: ByteArray,
) : AdnlAes {
    private val secretKey: SecretKey = SecretKeySpec(key, "AES")
    private val iv = IvParameterSpec(nonce)
    private val encryptCipher = Cipher.getInstance("AES/CTR/NoPadding").also {
        it.init(Cipher.ENCRYPT_MODE, secretKey, iv)
    }
    private val decryptCipher = Cipher.getInstance("AES/CTR/NoPadding").also {
        it.init(Cipher.DECRYPT_MODE, secretKey, iv)
    }

    override suspend fun encrypt(byteArray: ByteArray) = encryptCipher.doFinal(byteArray)

    override suspend fun decrypt(byteArray: ByteArray) = decryptCipher.doFinal(byteArray)
}

actual fun AdnlAes(key: ByteArray, nonce: ByteArray): AdnlAes = AdnlAesJvmImpl(key, nonce)