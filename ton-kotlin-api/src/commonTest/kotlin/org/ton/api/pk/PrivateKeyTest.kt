package org.ton.api.pk

import org.ton.crypto.base64.base64
import org.ton.crypto.hex
import kotlin.test.Test
import kotlin.test.assertContentEquals

class PrivateKeyTest {
    @Test
    fun `test creation PublicKey`() {
        val privateKeyEd25519 = PrivateKeyEd25519(base64("d53mOPj3+xx69TYJZ2LvzhxrNn32WBvt/ioV4Ha4gz8="))
        val publicKeyEd25519 = privateKeyEd25519.publicKey()
        assertContentEquals(
            hex("4745ede03eb4ef607843359c1f206d061a5632f68caa6f63021aa23b400950fd"),
            publicKeyEd25519.key
        )
    }
}
