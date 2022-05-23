package org.ton.block.tlb

import org.ton.block.VmCellSlice
import org.ton.block.VmCont
import org.ton.block.VmControlData
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.*

fun VmCont.Companion.tlbCodec(): TlbCodec<VmCont> = VmContTlbCombinator

private object VmContTlbCombinator : TlbCombinator<VmCont>(
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


    private object VmContStdTlbConstructor : TlbConstructor<VmCont.Std>(
        schema = "vmc_std\$00 cdata:VmControlData code:VmCellSlice = VmCont;"
    ) {
        override fun encode(
            cellBuilder: CellBuilder, value: VmCont.Std, param: Int, negativeParam: (Int) -> Unit
        ) = cellBuilder {
            storeTlb(value.cdata, VmControlData.tlbCodec())
            storeTlb(value.code, VmCellSlice.tlbCodec())
        }

        override fun decode(
            cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
        ): VmCont.Std = cellSlice {
            val cdata = loadTlb(VmControlData.tlbCodec())
            val code = loadTlb(VmCellSlice.tlbCodec())
            VmCont.Std(cdata, code)
        }
    }

    private object VmContEnvelopeTlbConstructor : TlbConstructor<VmCont.Envelope>(
        schema = "vmc_envelope\$01 cdata:VmControlData next:^VmCont = VmCont;"
    ) {
        private val vmControlDataCodec = VmControlData.tlbCodec()

        override fun encode(
            cellBuilder: CellBuilder, value: VmCont.Envelope, param: Int, negativeParam: (Int) -> Unit
        ) = cellBuilder {
            storeTlb(value.cdata, vmControlDataCodec)
            cellBuilder.storeRef {
                storeTlb(value, VmContTlbCombinator)
            }
        }

        override fun decode(
            cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
        ): VmCont.Envelope = cellSlice {
            val cdata = loadTlb(vmControlDataCodec)
            val next = loadRef {
                loadTlb(VmContTlbCombinator)
            }
            VmCont.Envelope(cdata, next)
        }
    }

    private object VmContQuitTlbConstructor : TlbConstructor<VmCont.Quit>(
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

    private object VmContQuitExcTlbConstructor : TlbConstructor<VmCont.QuitExc>(
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

    private object VmContRepeatTlbConstructor : TlbConstructor<VmCont.Repeat>(
        schema = "vmc_repeat\$10100 count:uint63 body:^VmCont after:^VmCont = VmCont;"
    ) {
        private val vmContCodec = VmCont.tlbCodec()

        override fun encode(
            cellBuilder: CellBuilder, value: VmCont.Repeat, param: Int, negativeParam: (Int) -> Unit
        ) = cellBuilder {
            storeUInt(value.count, 63)
            storeRef {
                storeTlb(value.body, vmContCodec)
            }
            storeRef {
                storeTlb(value.after, vmContCodec)
            }
        }

        override fun decode(
            cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
        ): VmCont.Repeat = cellSlice {
            val count = loadUInt(63).toLong()
            val body = loadRef {
                loadTlb(vmContCodec)
            }
            val after = loadRef {
                loadTlb(vmContCodec)
            }
            VmCont.Repeat(count, body, after)
        }
    }

    private object VmContUntilTlbConstructor : TlbConstructor<VmCont.Until>(
        schema = "vmc_until\$110000 body:^VmCont after:^VmCont = VmCont;"
    ) {
        private val vmContCodec = VmCont.tlbCodec()

        override fun encode(
            cellBuilder: CellBuilder, value: VmCont.Until, param: Int, negativeParam: (Int) -> Unit
        ) = cellBuilder {
            storeRef {
                storeTlb(value.body, vmContCodec)
            }
            storeRef {
                storeTlb(value.after, vmContCodec)
            }
        }

        override fun decode(
            cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
        ): VmCont.Until = cellSlice {
            val body = loadRef {
                loadTlb(vmContCodec)
            }
            val after = loadRef {
                loadTlb(vmContCodec)
            }
            VmCont.Until(body, after)
        }
    }

    private object VmContAgainTlbConstructor : TlbConstructor<VmCont.Again>(
        schema = "vmc_again\$110001 body:^VmCont = VmCont;"
    ) {
        private val vmContCodec = VmCont.tlbCodec()

        override fun encode(
            cellBuilder: CellBuilder, value: VmCont.Again, param: Int, negativeParam: (Int) -> Unit
        ) = cellBuilder {
            storeRef {
                storeTlb(value.body, vmContCodec)
            }
        }

        override fun decode(
            cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
        ): VmCont.Again = cellSlice {
            val body = loadRef {
                loadTlb(vmContCodec)
            }
            VmCont.Again(body)
        }
    }

    private object VmContWhileCondTlbConstructor : TlbConstructor<VmCont.WhileCond>(
        schema = "vmc_while_cond\$110010 cond:^VmCont body:^VmCont after:^VmCont = VmCont;"
    ) {
        private val vmContCodec = VmCont.tlbCodec()

        override fun encode(
            cellBuilder: CellBuilder, value: VmCont.WhileCond, param: Int, negativeParam: (Int) -> Unit
        ) = cellBuilder {
            storeRef {
                storeTlb(value.cond, vmContCodec)
            }
            storeRef {
                storeTlb(value.body, vmContCodec)
            }
            storeRef {
                storeTlb(value.after, vmContCodec)
            }
        }

        override fun decode(
            cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
        ): VmCont.WhileCond = cellSlice {
            val cond = loadRef {
                loadTlb(vmContCodec)
            }
            val body = loadRef {
                loadTlb(vmContCodec)
            }
            val after = loadRef {
                loadTlb(vmContCodec)
            }
            VmCont.WhileCond(cond, body, after)
        }
    }

    private object VmContWhileBodyTlbConstructor : TlbConstructor<VmCont.WhileBody>(
        schema = "vmc_while_body\$110011 cond:^VmCont body:^VmCont after:^VmCont = VmCont;"
    ) {
        private val vmContCodec = VmCont.tlbCodec()

        override fun encode(
            cellBuilder: CellBuilder, value: VmCont.WhileBody, param: Int, negativeParam: (Int) -> Unit
        ) = cellBuilder {
            storeRef {
                storeTlb(value.cond, vmContCodec)
            }
            storeRef {
                storeTlb(value.body, vmContCodec)
            }
            storeRef {
                storeTlb(value.after, vmContCodec)
            }
        }

        override fun decode(
            cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
        ): VmCont.WhileBody = cellSlice {
            val cond = loadRef {
                loadTlb(vmContCodec)
            }
            val body = loadRef {
                loadTlb(vmContCodec)
            }
            val after = loadRef {
                loadTlb(vmContCodec)
            }
            VmCont.WhileBody(cond, body, after)
        }
    }

    private object VmContPushIntTlbConstructor : TlbConstructor<VmCont.PushInt>(
        schema = "vmc_pushint\$1111 value:int32 next:^VmCont = VmCont;"
    ) {
        private val vmContCodec = VmCont.tlbCodec()

        override fun encode(
            cellBuilder: CellBuilder, value: VmCont.PushInt, param: Int, negativeParam: (Int) -> Unit
        ) = cellBuilder {
            storeInt(value.value, 32)
            storeRef {
                storeTlb(value.next, vmContCodec)
            }
        }

        override fun decode(
            cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
        ): VmCont.PushInt = cellSlice {
            val value = loadInt(32).toInt()
            val next = loadTlb(vmContCodec)
            VmCont.PushInt(value, next)
        }
    }
}
