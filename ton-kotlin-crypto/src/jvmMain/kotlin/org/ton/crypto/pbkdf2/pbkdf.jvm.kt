package org.ton.crypto.pbkdf2

import org.bouncycastle.crypto.digests.SHA512Digest
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator
import org.bouncycastle.crypto.params.KeyParameter

actual fun pbkdf2Sha512(key: ByteArray, salt: ByteArray, iterations: Int): ByteArray {
    val gen = PKCS5S2ParametersGenerator(SHA512Digest())
    gen.init(key, salt, iterations)
    return (gen.generateDerivedParameters(512) as KeyParameter).key
}
