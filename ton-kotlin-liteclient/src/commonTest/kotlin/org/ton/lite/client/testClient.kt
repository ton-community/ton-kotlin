package org.ton.lite.client

import io.ktor.utils.io.bits.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import org.ton.crypto.crc32.*
import org.ton.crypto.encodeHex
import org.ton.crypto.sha256.sha256
import org.ton.lite.api.liteserver.functions.LiteServerGetMasterchainInfo
import kotlin.test.Test

class TestClient {
    // crc32 = 7af98bb4 - good
    // crc32 = 4c8c5668 - bad
    // crc32c = 145f013d
    @Test
    fun test() = runBlocking {

//        val string = "adnl.message.query query_id:int256 query:bytes = adnl.Message".toByteArray()
////        println("crc32 = ${crc32(string).reverseByteOrder().toUInt().toString(16)}")
////        println("crc32c = ${crc32c(string).reverseByteOrder().toUInt().toString(16)}")
//    //        println("crc32 = ${crc32("liteServer.query data:bytes = Object".encodeToByteArray()).toUInt().toString(16)}")
        val adnlLazyClient = AdnlLazyClientImpl(
            newSingleThreadContext("adnl"),
            GLOBAL_CONFIG
        )
        val liteClient = LiteClientImpl(adnlLazyClient)
        val result = liteClient.sendQuery(LiteServerGetMasterchainInfo)
        println(result)
    }
}
