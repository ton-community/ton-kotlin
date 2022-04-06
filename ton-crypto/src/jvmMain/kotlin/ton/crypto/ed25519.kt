package ton.crypto

import curve25519.CompressedEdwardsY
import kotlin.random.Random

object Ed25519 {
    fun generatePrivateKey(random: Random = Random.Default): ByteArray = random.nextBytes(32)

    fun generatePublicKey(privateKey: ByteArray): ByteArray {
        val publicKey = ByteArray(32)
        org.bouncycastle.math.ec.rfc8032.Ed25519.generatePublicKey(privateKey, 0, publicKey, 0)
        return publicKey
    }
}

object X25519 {
    fun generatePrivateKey(random: Random = Random.Default): ByteArray = random.nextBytes(32)

    fun generatePublicKey(privateKey: ByteArray): ByteArray {
        val publicKey = ByteArray(32)
        org.bouncycastle.math.ec.rfc7748.X25519.generatePublicKey(privateKey, 0, publicKey, 0)
        return publicKey
    }

    fun calculateAgreement(privateKey: ByteArray, publicKey: ByteArray): ByteArray {
        val agreement = ByteArray(32)
        org.bouncycastle.math.ec.rfc7748.X25519.calculateAgreement(privateKey, 0, publicKey, 0, agreement, 0)
        return agreement
    }
}

fun byteArrayOf(vararg ints: Int) = ints.toByteArray()

val EXPECTED_PRIVATE = hex("6162616361626164616261636162616561626163616261646162616361626100")
val EXPECTED_PUBLIC = hex("6F9E5BDECE8721EB5737FBB59228BA07F7880F73CE5BFAA1B7157303D4201E74")
val EXPECTED_SHARED = hex("24451FC42D64B73BFC73BEAE8AEDFE20E62D221315C672A669A9BC13B4913B1F")

/*
private =
public =
shared = 24451FC42D64B73BFC73BEAE8AEDFE20E62D221315C672A669A9BC13B4913B1F
 */


fun main() {
    val private = hex("d08fb02f0dc3e167d6ff114721c4456079310c3141f46f11bdd148fc8c7ef364")
    val public = Crypto.publicKey(private)
    val nicePublic = CompressedEdwardsY(public).decompress()!!.toMontgomeryPoint().toByteArray()
    println(hex(nicePublic))
}