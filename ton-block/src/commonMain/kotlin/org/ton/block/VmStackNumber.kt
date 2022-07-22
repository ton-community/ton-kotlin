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
}