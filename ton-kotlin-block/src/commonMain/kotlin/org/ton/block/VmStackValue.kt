@file:Suppress("OPT_IN_USAGE", "NOTHING_TO_INLINE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.bigint.BigInt
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCombinator
import org.ton.tlb.providers.TlbCombinatorProvider
import kotlin.jvm.JvmStatic

inline fun VmStackValue(): VmStackValue = VmStackValue.of()
inline fun VmStackValue(int: Int): VmStackTinyInt = VmStackValue.of(int)
inline fun VmStackValue(long: Long): VmStackTinyInt = VmStackValue.of(long)
inline fun VmStackValue(boolean: Boolean): VmStackTinyInt = VmStackValue.of(boolean)
inline fun VmStackValue(bigInt: BigInt): VmStackInt = VmStackValue.of(bigInt)
inline fun VmStackValue(cell: Cell): VmStackCell = VmStackValue.of(cell)
inline fun VmStackValue(cellSlice: CellSlice): VmCellSlice = VmStackValue.of(cellSlice)
inline fun VmStackValue(cellBuilder: CellBuilder): VmStackBuilder = VmStackValue.of(cellBuilder)
inline fun VmStackValue(cont: VmCont): VmStackCont = VmStackValue.of(cont)
inline fun VmStackValue(tuple: VmTuple): VmStackTuple = VmStackValue.of(tuple)

@JsonClassDiscriminator("@type")
@Serializable
sealed interface VmStackValue {
    companion object : TlbCombinatorProvider<VmStackValue> by VmStackValueTlbCombinator {
        @JvmStatic
        fun of(): VmStackNull = VmStackNull

        @JvmStatic
        fun of(int: Int): VmStackTinyInt = VmStackTinyInt(int)

        @JvmStatic
        fun of(boolean: Boolean): VmStackTinyInt = VmStackTinyInt(boolean)

        @JvmStatic
        fun of(long: Long): VmStackTinyInt = VmStackTinyInt(long)

        @JvmStatic
        fun of(bigInt: BigInt): VmStackInt = VmStackInt(bigInt)

        @JvmStatic
        fun of(cell: Cell): VmStackCell = VmStackCell(cell)

        @JvmStatic
        fun of(cellSlice: CellSlice): VmCellSlice = VmCellSlice(cellSlice)

        @JvmStatic
        fun of(cellBuilder: CellBuilder): VmStackBuilder = VmStackBuilder(cellBuilder)

        @JvmStatic
        fun of(cont: VmCont): VmStackCont = VmStackCont(cont)

        @JvmStatic
        fun of(tuple: VmTuple): VmStackTuple = VmStackTuple(tuple)
    }
}

private object VmStackValueTlbCombinator : TlbCombinator<VmStackValue>(
    VmStackValue::class,
    VmStackNull::class to VmStackNull.tlbConstructor(),
    VmStackTinyInt::class to VmStackTinyInt.tlbConstructor(),
    VmStackInt::class to VmStackInt.tlbConstructor(),
    VmStackNan::class to VmStackNan.tlbConstructor(),
    VmStackCell::class to VmStackCell.tlbConstructor(),
    VmCellSlice::class to VmCellSlice.tlbConstructor(),
    VmStackBuilder::class to VmStackBuilder.tlbConstructor(),
    VmStackCont::class to VmStackCont.tlbConstructor(),
    VmStackTuple::class to VmStackTuple.tlbConstructor()
)
