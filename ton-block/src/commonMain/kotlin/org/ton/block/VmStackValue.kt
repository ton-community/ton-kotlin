@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.bigint.BigInt
import org.ton.bigint.BigIntSerializer

@JsonClassDiscriminator("@type")
@Serializable
sealed interface VmStackValue {
    @SerialName("vm_stk_null")
    @Serializable
    object Null : VmStackValue

    @SerialName("vm_stk_tinyint")
    @Serializable
    data class TinyInt(
        val value: Long
    ) : VmStackValue {
        constructor(byte: Byte) : this(byte.toLong())
        constructor(short: Short) : this(short.toLong())
        constructor(int: kotlin.Int) : this(int.toLong())
    }

    @SerialName("vm_stk_int")
    @Serializable
    data class Int(
        @Serializable(BigIntSerializer::class)
        val value: BigInt
    ) : VmStackValue {
        constructor(byte: Byte) : this(BigInt(byte))
        constructor(short: Short) : this(BigInt(short))
        constructor(int: kotlin.Int) : this(BigInt(int))
        constructor(long: Long) : this(BigInt(long))
    }

    @SerialName("vm_stk_nan")
    object Nan : VmStackValue

    @SerialName("vm_stk_cell")
    data class Cell(
        val cell: org.ton.cell.Cell
    ) : VmStackValue

    @SerialName("vm_stk_slice")
    data class Slice(
        @SerialName("_")
        val slice: VmCellSlice
    ) : VmStackValue

    @SerialName("vm_stk_builder")
    data class Builder(
        val cell: org.ton.cell.Cell
    ) : VmStackValue

    @SerialName("vm_stk_cont")
    data class Cont(
        val cont: VmCont
    ) : VmStackValue

    @SerialName("vm_stk_tuple")
    data class Tuple(
        val len: kotlin.Int,
        val data: VmTuple
    ) : VmStackValue
}
