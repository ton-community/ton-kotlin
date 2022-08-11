package org.ton.api

import org.ton.api.pk.PrivateKey
import org.ton.api.pub.PublicKey
import org.ton.tl.TlObject

interface SignedTlObject<T : TlObject<T>> : TlObject<T> {
    val signature: ByteArray?

    fun signed(privateKey: PrivateKey): T

    fun verify(publicKey: PublicKey): Boolean
}