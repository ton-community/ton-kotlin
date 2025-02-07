import io.github.andreypfau.curve25519.ed25519.Ed25519
import kotlin.test.Test

class Ed25519Test {
    @Test
    fun testSign() {
        val privateKey = Ed25519.keyFromSeed(
            "a184a4ec4c9d1f65dba578bb8f18e494b9c154d21941c209741f55283ad38353".hexToByteArray()
        )
        val publicKey = privateKey.publicKey()
        val sig = privateKey.sign("test".encodeToByteArray())
        val verified = publicKey.verify("test".encodeToByteArray(), sig)
        println("${sig.toHexString()} verified: $verified")
    }
}