package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.*
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@SerialName("vmc_repeat")
@Serializable
data class VmContRepeat(
    val count: Long,
    val body: VmCont,
    val after: VmCont
) : VmCont {
    companion object {
        fun tlbConstructor(): TlbConstructor<VmContRepeat> = VmContRepeatTlbConstructor
    }
}

private object VmContRepeatTlbConstructor : TlbConstructor<VmContRepeat>(
    schema = "vmc_repeat\$10100 count:uint63 body:^VmCont after:^VmCont = VmCont;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder, value: VmContRepeat
    ) = cellBuilder {
        storeUInt(value.count, 63)
        storeRef {
            storeTlb(VmCont, value.body)
        }
        storeRef {
            storeTlb(VmCont, value.after)
        }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): VmContRepeat = cellSlice {
        val count = loadUInt(63).toLong()
        val body = loadRef {
            loadTlb(VmCont)
        }
        val after = loadRef {
            loadTlb(VmCont)
        }
        VmContRepeat(count, body, after)
    }
}