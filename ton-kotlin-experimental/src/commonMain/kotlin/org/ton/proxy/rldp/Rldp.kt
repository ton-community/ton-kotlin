package org.ton.proxy.rldp

import io.ktor.utils.io.*
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.ton.api.adnl.AdnlIdShort
import org.ton.bitstring.BitString
import org.ton.proxy.adnl.Adnl
import org.ton.proxy.adnl.AdnlSender
import kotlin.time.Duration

class Rldp(
    val adnl: Adnl
) : CoroutineScope, AdnlSender {
    override val coroutineContext = Dispatchers.Default + CoroutineName(toString())
    private val transfers = mutableMapOf<BitString, RldpOutputTransfer>()


    override suspend fun message(destination: AdnlIdShort, payload: ByteArray) {

    }

    override suspend fun query(
        destination: AdnlIdShort,
        payload: ByteArray,
        timeout: Duration,
        maxAnswerSize: Int
    ): ByteArray {
        TODO("Not yet implemented")
    }


}
