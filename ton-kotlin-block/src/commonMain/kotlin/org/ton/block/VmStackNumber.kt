package org.ton.block

import org.ton.bigint.BigInt

sealed interface VmStackNumber : VmStackValue {
    fun toInt(): Int
    fun toLong(): Long
    fun toBoolean(): Boolean
    fun toBigInt(): BigInt

    operator fun plus(other: VmStackNumber): VmStackNumber
    operator fun minus(other: VmStackNumber): VmStackNumber
    operator fun times(other: VmStackNumber): VmStackNumber
    operator fun div(other: VmStackNumber): VmStackNumber
    operator fun unaryMinus(): VmStackNumber
    operator fun inc(): VmStackNumber = this + VmStackValue(1)
    operator fun dec(): VmStackNumber = this - VmStackValue(1)
}