@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.cell.*
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@JsonClassDiscriminator("@type")
@Serializable
sealed interface VmTuple {
    companion object {
        @JvmStatic
        fun tlbCodec(n: Int): TlbCombinator<VmTuple> = VmTupleTlbCombinator(n)
    }
}

@SerialName("vm_tuple_nil")
@Serializable
object VmTupleNil : VmTuple {
    fun tlbCodec(): TlbConstructor<VmTupleNil> = VmTupleNilTlbConstructor
}

@SerialName("vm_tuple_tcons")
@Serializable
data class VmTupleTcons(
    val head: VmTupleRef,
    val tail: VmStackValue
) : VmTuple {
    companion object {
        @JvmStatic
        fun tlbCodec(n: Int): TlbConstructor<VmTupleTcons> = VmTupleTconsTlbConstructor(n)
    }
}

private class VmTupleTlbCombinator(val n: Int) : TlbCombinator<VmTuple>() {
    val vmTupleNil by lazy { VmTupleNil.tlbCodec() }
    val vmTupleTcons by lazy { VmTupleTcons.tlbCodec(n) }

    override val constructors: List<TlbConstructor<out VmTuple>> by lazy {
        listOf(vmTupleNil, vmTupleTcons)
    }

    override fun getConstructor(value: VmTuple): TlbConstructor<out VmTuple> = when (value) {
        is VmTupleNil -> vmTupleNil
        is VmTupleTcons -> vmTupleTcons
    }

    override fun loadTlb(cellSlice: CellSlice): VmTuple {
        return if (n == 0) {
            vmTupleNil.loadTlb(cellSlice)
        } else {
            vmTupleTcons.loadTlb(cellSlice)
        }
    }
}

object VmTupleNilTlbConstructor : TlbConstructor<VmTupleNil>(
    schema = "vm_tuple_nil\$_ = VmTuple 0;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: VmTupleNil) {
    }

    override fun loadTlb(cellSlice: CellSlice): VmTupleNil = VmTupleNil
}

private class VmTupleTconsTlbConstructor(n: Int) : TlbConstructor<VmTupleTcons>(
    schema = "vm_tuple_tcons\$_ {n:#} head:(VmTupleRef n) tail:^VmStackValue = VmTuple (n + 1);"
) {
    private val vmTupleRef by lazy {
        VmTupleRef.tlbCodec(n - 1)
    }
    private val vmStackValue by lazy {
        VmStackValue.tlbCodec()
    }

    override fun storeTlb(cellBuilder: CellBuilder, value: VmTupleTcons) = cellBuilder {
        storeTlb(vmTupleRef, value.head)
        storeTlb(vmStackValue, value.tail)
    }

    override fun loadTlb(cellSlice: CellSlice): VmTupleTcons = cellSlice {
        val head = loadTlb(vmTupleRef)
        val tail = loadTlb(vmStackValue)
        VmTupleTcons(head, tail)
    }
}