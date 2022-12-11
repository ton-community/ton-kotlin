package org.ton.adnl.connection

import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import org.ton.adnl.query.AdnlQueryId
import org.ton.api.adnl.message.AdnlMessageAnswer
import org.ton.api.pk.PrivateKey
import org.ton.api.pub.PublicKey
import org.ton.api.tcp.TcpAuthentificate
import org.ton.api.tcp.TcpAuthentificationComplete
import org.ton.api.tcp.TcpAuthentificationNonce
import org.ton.api.tcp.TcpPong
import org.ton.crypto.random.SecureRandom
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

class AdnlOutboundConnection(
    coroutineContext: CoroutineContext,
    inputChannel: ByteReadChannel,
    outputChannel: ByteWriteChannel,
    val client: AdnlClientImpl,
    val remoteKey: PublicKey,
    val localKey: PrivateKey? = null,
    onDone: () -> Unit
) : AdnlConnection(
    true,
    inputChannel,
    outputChannel,
    coroutineContext,
    onDone
) {
    private var authorizationComplete: Boolean = false
    private var nonce: ByteArray? = null
    val handshakeJob = launch {
        handshake()
    }
    val receiveJob = launch(coroutineContext + CoroutineName("receive")) {
        handshakeJob.join()
        while (true) {
            receive(inputChannel)
        }
    }

    override suspend fun handleInitPacket(data: ByteReadPacket) = error("Unexpected init packet")

    override suspend fun handleCustomPacket(data: ByteReadPacket): Boolean {
        if (data.remaining == 12L) {
            try {
                val tcpPong = TcpPong.decodeBoxed(data.copy())
                return true
            } catch (ignored: Exception) {
            }
        }
        val nonce = nonce
        if (localKey != null && nonce != null) {
            try {
                val remoteNonce = TcpAuthentificationNonce.decodeBoxed(data.copy()).nonce
                require(!(remoteNonce.isEmpty() || remoteNonce.size > 512)) { "Invalid nonce size: ${remoteNonce.size}" }
                val ss = ByteArray(nonce.size + remoteNonce.size)
                nonce.copyInto(ss)
                remoteNonce.copyInto(ss, nonce.size)
                val signature = localKey.sign(ss)
                send {
                    TcpAuthentificationComplete.encodeBoxed(
                        this,
                        TcpAuthentificationComplete(localKey.publicKey(), signature)
                    )
                }
                this.nonce = null
                return true
            } catch (ignored: Exception) {
            }
        }
        return false
    }

    override suspend fun handlePacket(data: ByteReadPacket) {
        val adnlMessageAnswer = AdnlMessageAnswer.decodeBoxed(data)
        client.answerQuery(AdnlQueryId(adnlMessageAnswer.query_id), ByteReadPacket(adnlMessageAnswer.answer))
    }

    private suspend fun handshake() {
        val id = remoteKey.toAdnlIdShort().id
        val nonce = SecureRandom.nextBytes(160) // TODO: return random
        initCrypto(nonce)

        val data = buildPacket {
            writeFully(id)
            writeFully(remoteKey.encrypt(nonce))
        }

        sendRaw(data)

        if (localKey != null) {
            this@AdnlOutboundConnection.nonce = SecureRandom.nextBytes(32)
            val tcpAuthentificate = TcpAuthentificate(nonce)
            send {
                TcpAuthentificate.encodeBoxed(this, tcpAuthentificate)
            }
        }
    }
}
