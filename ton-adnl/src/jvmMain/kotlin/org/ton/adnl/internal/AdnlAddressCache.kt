package org.ton.adnl.internal

import io.ktor.util.collections.*
import kotlinx.atomicfu.atomic
import org.ton.api.pub.PublicKey
import org.ton.logger.Logger
import kotlin.math.min
import kotlin.random.Random

class AdnlAddressCache(
    val cache: MutableMap<PublicKey, Int> = ConcurrentMap(),
    val index: MutableMap<Int, PublicKey> = ConcurrentMap(),
    val limit: Int,
    val logger: Logger = Logger.println("AdnlAddressCache")
) : Iterable<PublicKey> by cache.keys {
    var upper = atomic(0)
    val count get() = min(upper.value, limit)

    fun put(address: PublicKey): Boolean {
        var result = false
        val index = cache.getOrPut(address) {
            result = true
            val upper = upper.getAndIncrement()
            var resultIndex = upper
            if (resultIndex >= limit) {
                if (resultIndex >= limit * 2) {
                    this.upper.compareAndSet(
                        upper + 1,
                        resultIndex - limit + 1
                    )
                }
                resultIndex %= limit
            }
            resultIndex
        }
        this.index[index] = address
        return result
    }

    fun get(index: Int): PublicKey? = this.index[index]

    fun random(skip: Collection<PublicKey>? = null): PublicKey? {
        val max = count
        // We need a finite loop here because we can test skip set only on case-by-case basis
        // due to multithreading. So it is possible that all items shall be skipped, and with
        // infinite loop we will simply hang
        repeat(10) {
            val result = index[Random.nextInt(max)]
            if (result != null) {
                if (skip != null) {
                    if (result !in skip) {
                        return result
                    }
                } else {
                    return result
                }
            }
        }
        return null
    }
}