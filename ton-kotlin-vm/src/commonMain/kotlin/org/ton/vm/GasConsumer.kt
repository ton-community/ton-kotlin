package org.ton.vm

import org.ton.block.VmGasLimits
import org.ton.block.VmStack
import kotlin.math.max

public class GasConsumer(
    public var limits: VmGasLimits = VmGasLimits(),
    public val price: GasPrice = GasPrice()
) {
    public fun consume(amount: Int) = consume(amount.toLong())

    public fun consume(amount: Long) {
        limits = limits.consume(amount)
//        println("consumed: $amount (remaining ${limits.remaining})")
    }

    public fun consumeStack(stackDepth: Int) {
        consume((max(stackDepth.toLong(), price.freeStack) - price.freeStack) * price.stackEntry)
    }

    public fun consumeStack(stack: VmStack?) {
        if (stack != null) {
            consumeStack(stack.size)
        }
    }

    public data class GasPrice(
        val cellLoad: Long = 100,
        val cellReload: Long = 25,
        val cellCreate: Long = 500,
        val exception: Long = 50,
        val tupleEntry: Long = 1,
        val implicitJmpRef: Long = 10,
        val implicitRet: Long = 5,
        val freeStack: Long = 32,
        val stackEntry: Long = 1
    )
}
