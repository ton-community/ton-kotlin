package org.ton.block.tlb

import org.ton.block.VmCont
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

object VmContTlbCombinator : TlbCombinator<VmCont>(
    VmContStdTlbConstructor,
    VmContEnvelopeTlbConstructor,
    VmContQuitTlbConstructor,
    VmContQuitExcTlbConstructor,
    VmContRepeatTlbConstructor,
    VmContUntilTlbConstructor,
    VmContAgainTlbConstructor,
    VmContWhileCondTlbConstructor,
    VmContWhileBodyTlbConstructor,
    VmContPushIntTlbConstructor
) {
    override fun getConstructor(value: VmCont): TlbConstructor<out VmCont> = when (value) {
        is VmCont.Again -> VmContAgainTlbConstructor
        is VmCont.Envelope -> VmContEnvelopeTlbConstructor
        is VmCont.PushInt -> VmContPushIntTlbConstructor
        is VmCont.Quit -> VmContQuitTlbConstructor
        is VmCont.QuitExc -> VmContQuitExcTlbConstructor
        is VmCont.Repeat -> VmContRepeatTlbConstructor
        is VmCont.Std -> VmContStdTlbConstructor
        is VmCont.Until -> VmContUntilTlbConstructor
        is VmCont.WhileBody -> VmContWhileBodyTlbConstructor
        is VmCont.WhileCond -> VmContWhileCondTlbConstructor
    }
}

object VmContStdTlbConstructor : TlbConstructor<VmCont.Std>(
    schema = "vmc_std\$00 cdata:VmControlData code:VmCellSlice = VmCont;"
) {
    override fun encode(
        cellBuilder: CellBuilder, value: VmCont.Std, param: Int, negativeParam: (Int) -> Unit
    ) = cellBuilder {
        storeTlb(value.cdata, VmControlDataTlbConstructor)
        storeTlb(value.code, VmCellSliceTlbConstructor)
    }

    override fun decode(
        cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
    ): VmCont.Std = cellSlice {
        val cdata = loadTlb(VmControlDataTlbConstructor)
        val code = loadTlb(VmCellSliceTlbConstructor)
        VmCont.Std(cdata, code)
    }
}

object VmContEnvelopeTlbConstructor : TlbConstructor<VmCont.Envelope>(
    schema = "vmc_envelope\$01 cdata:VmControlData next:^VmCont = VmCont;"
) {
    override fun encode(
        cellBuilder: CellBuilder, value: VmCont.Envelope, param: Int, negativeParam: (Int) -> Unit
    ) = cellBuilder {
        storeTlb(value.cdata, VmControlDataTlbConstructor)
        cellBuilder.storeRef {
            storeTlb(value, VmContTlbCombinator)
        }
    }

    override fun decode(
        cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
    ): VmCont.Envelope = cellSlice {
        val cdata = loadTlb(VmControlDataTlbConstructor)
        val next = loadRef {
            loadTlb(VmContTlbCombinator)
        }
        VmCont.Envelope(cdata, next)
    }
}

object VmContQuitTlbConstructor : TlbConstructor<VmCont.Quit>(
    schema = "vmc_quit\$1000 exit_code:int32 = VmCont;"
) {
    override fun encode(
        cellBuilder: CellBuilder, value: VmCont.Quit, param: Int, negativeParam: (Int) -> Unit
    ) = cellBuilder {
        storeInt(value.exitCode, 32)
    }

    override fun decode(
        cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
    ): VmCont.Quit = cellSlice {
        val exitCode = loadInt(32).toInt()
        VmCont.Quit(exitCode)
    }
}

object VmContQuitExcTlbConstructor : TlbConstructor<VmCont.QuitExc>(
    schema = "vmc_quit_exc\$1001 = VmCont;"
) {
    override fun encode(
        cellBuilder: CellBuilder, value: VmCont.QuitExc, param: Int, negativeParam: (Int) -> Unit
    ) {
    }

    override fun decode(
        cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
    ): VmCont.QuitExc = VmCont.QuitExc
}

object VmContRepeatTlbConstructor : TlbConstructor<VmCont.Repeat>(
    schema = "vmc_repeat\$10100 count:uint63 body:^VmCont after:^VmCont = VmCont;"
) {
    override fun encode(
        cellBuilder: CellBuilder, value: VmCont.Repeat, param: Int, negativeParam: (Int) -> Unit
    ) = cellBuilder {
        storeUInt(value.count, 63)
        storeRef {
            storeTlb(value.body, VmContTlbCombinator)
        }
        storeRef {
            storeTlb(value.after, VmContTlbCombinator)
        }
    }

    override fun decode(
        cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
    ): VmCont.Repeat = cellSlice {
        val count = loadUInt(63).toLong()
        val body = loadRef {
            loadTlb(VmContTlbCombinator)
        }
        val after = loadRef {
            loadTlb(VmContTlbCombinator)
        }
        VmCont.Repeat(count, body, after)
    }
}

object VmContUntilTlbConstructor : TlbConstructor<VmCont.Until>(
    schema = "vmc_until\$110000 body:^VmCont after:^VmCont = VmCont;"
) {
    override fun encode(
        cellBuilder: CellBuilder, value: VmCont.Until, param: Int, negativeParam: (Int) -> Unit
    ) = cellBuilder {
        storeRef {
            storeTlb(value.body, VmContTlbCombinator)
        }
        storeRef {
            storeTlb(value.after, VmContTlbCombinator)
        }
    }

    override fun decode(
        cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
    ): VmCont.Until = cellSlice {
        val body = loadRef {
            loadTlb(VmContTlbCombinator)
        }
        val after = loadRef {
            loadTlb(VmContTlbCombinator)
        }
        VmCont.Until(body, after)
    }
}

object VmContAgainTlbConstructor : TlbConstructor<VmCont.Again>(
    schema = "vmc_again\$110001 body:^VmCont = VmCont;"
) {
    override fun encode(
        cellBuilder: CellBuilder, value: VmCont.Again, param: Int, negativeParam: (Int) -> Unit
    ) = cellBuilder {
        storeRef {
            storeTlb(value.body, VmContTlbCombinator)
        }
    }

    override fun decode(
        cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
    ): VmCont.Again = cellSlice {
        val body = loadRef {
            loadTlb(VmContTlbCombinator)
        }
        VmCont.Again(body)
    }
}

object VmContWhileCondTlbConstructor : TlbConstructor<VmCont.WhileCond>(
    schema = "vmc_while_cond\$110010 cond:^VmCont body:^VmCont after:^VmCont = VmCont;"
) {
    override fun encode(
        cellBuilder: CellBuilder, value: VmCont.WhileCond, param: Int, negativeParam: (Int) -> Unit
    ) = cellBuilder {
        storeRef {
            storeTlb(value.cond, VmContTlbCombinator)
        }
        storeRef {
            storeTlb(value.body, VmContTlbCombinator)
        }
        storeRef {
            storeTlb(value.after, VmContTlbCombinator)
        }
    }

    override fun decode(
        cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
    ): VmCont.WhileCond = cellSlice {
        val cond = loadRef {
            loadTlb(VmContTlbCombinator)
        }
        val body = loadRef {
            loadTlb(VmContTlbCombinator)
        }
        val after = loadRef {
            loadTlb(VmContTlbCombinator)
        }
        VmCont.WhileCond(cond, body, after)
    }
}

object VmContWhileBodyTlbConstructor : TlbConstructor<VmCont.WhileBody>(
    schema = "vmc_while_body\$110011 cond:^VmCont body:^VmCont after:^VmCont = VmCont;"
) {
    override fun encode(
        cellBuilder: CellBuilder, value: VmCont.WhileBody, param: Int, negativeParam: (Int) -> Unit
    ) = cellBuilder {
        storeRef {
            storeTlb(value.cond, VmContTlbCombinator)
        }
        storeRef {
            storeTlb(value.body, VmContTlbCombinator)
        }
        storeRef {
            storeTlb(value.after, VmContTlbCombinator)
        }
    }

    override fun decode(
        cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
    ): VmCont.WhileBody = cellSlice {
        val cond = loadRef {
            loadTlb(VmContTlbCombinator)
        }
        val body = loadRef {
            loadTlb(VmContTlbCombinator)
        }
        val after = loadRef {
            loadTlb(VmContTlbCombinator)
        }
        VmCont.WhileBody(cond, body, after)
    }
}

object VmContPushIntTlbConstructor : TlbConstructor<VmCont.PushInt>(
    schema = "vmc_pushint\$1111 value:int32 next:^VmCont = VmCont;"
) {
    override fun encode(
        cellBuilder: CellBuilder, value: VmCont.PushInt, param: Int, negativeParam: (Int) -> Unit
    ) = cellBuilder {
        storeInt(value.value, 32)
        storeRef {
            storeTlb(value.next, VmContTlbCombinator)
        }
    }

    override fun decode(
        cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
    ): VmCont.PushInt = cellSlice {
        val value = loadInt(32).toInt()
        val next = loadTlb(VmContTlbCombinator)
        VmCont.PushInt(value, next)
    }
}
