@file:OptIn(ExperimentalTime::class)

package org.ton.lite.client

import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import kotlin.test.Test
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class TestClient {
    // crc32 = 7af98bb4 - good
    // crc32 = 4c8c5668 - bad
    // crc32c = 145f013d
    @Test
    fun test() = runBlocking {
        val liteClient = LiteClient(newSingleThreadContext("liteclient"), GLOBAL_CONFIG)
        while (true) {
            try {
                println(liteClient.getServerTime())
            } catch (e: Exception) {
                println(e)
            }
        }
    }
}
