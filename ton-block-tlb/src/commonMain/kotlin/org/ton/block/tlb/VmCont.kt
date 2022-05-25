package org.ton.block.tlb

import org.ton.block.VmCellSlice
import org.ton.block.VmCont
import org.ton.block.VmControlData
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.*

fun VmCont.Companion.tlbCodec(): TlbCodec<VmCont> = VmContTlbCombinator()

private class VmContTlbCombinator : TlbCombinator<VmCont>() {

    private val stdConstructor by lazy {
        VmContStdTlbConstructor()
    }
    private val envelopeConstructor by lazy {
        VmContEnvelopeTlbConstructor()
    }
    private val quitConstructor by lazy {
        VmContQuitTlbConstructor()
    }
    private val quitExcConstructor by lazy {
        VmContQuitExcTlbConstructor()
    }
    private val repeatConstructor by lazy {
        VmContRepeatTlbConstructor()
    }
    private val untilConstructor by lazy {
        VmContUntilTlbConstructor()
    }
    private val againConstructor by lazy {
        VmContAgainTlbConstructor()
    }
    private val whileBodyConstructor by lazy {
        VmContWhileBodyTlbConstructor()
    }
    private val whileCondConstructor by lazy {
        VmContWhileCondTlbConstructor()
    }
    private val pushIntConstructor by lazy {
        VmContPushIntTlbConstructor()
    }

    override val constructors: List<TlbConstructor<out VmCont>> by lazy {
        listOf(
            stdConstructor, envelopeConstructor, quitConstructor, quitExcConstructor, repeatConstructor,
            untilConstructor, againConstructor, whileBodyConstructor, whileCondConstructor, pushIntConstructor
        )
    }

    override fun getConstructor(value: VmCont): TlbConstructor<out VmCont> = when (value) {
        is VmCont.Std -> stdConstructor
        is VmCont.Envelope -> envelopeConstructor
        is VmCont.Quit -> quitConstructor
        is VmCont.QuitExc -> quitExcConstructor
        is VmCont.Repeat -> repeatConstructor
        is VmCont.Until -> untilConstructor
        is VmCont.Again -> againConstructor
        is VmCont.WhileBody -> whileBodyConstructor
        is VmCont.WhileCond -> whileCondConstructor
        is VmCont.PushInt -> pushIntConstructor
    }

    private class VmContStdTlbConstructor : TlbConstructor<VmCont.Std>(
        schema = "vmc_std\$00 cdata:VmControlData code:VmCellSlice = VmCont;"
    ) {
        private val vmControlDataCodec by lazy {
            VmControlData.tlbCodec()
        }
        private val vmCellSliceCodec by lazy {
            VmCellSlice.tlbCodec()
        }

        override fun storeTlb(
            cellBuilder: CellBuilder, value: VmCont.Std
        ) = cellBuilder {
            storeTlb(vmControlDataCodec, value.cdata)
            storeTlb(vmCellSliceCodec, value.code)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): VmCont.Std = cellSlice {
            val cdata = loadTlb(vmControlDataCodec)
            val code = loadTlb(vmCellSliceCodec)
            VmCont.Std(cdata, code)
        }
    }

    private class VmContEnvelopeTlbConstructor : TlbConstructor<VmCont.Envelope>(
        schema = "vmc_envelope\$01 cdata:VmControlData next:^VmCont = VmCont;"
    ) {
        private val vmControlDataCodec by lazy {
            VmControlData.tlbCodec()
        }
        private val vmContCodec by lazy {
            VmCont.tlbCodec()
        }

        override fun storeTlb(
            cellBuilder: CellBuilder, value: VmCont.Envelope
        ) = cellBuilder {
            storeTlb(vmControlDataCodec, value.cdata)
            cellBuilder.storeRef {
                storeTlb(vmContCodec, value)
            }
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): VmCont.Envelope = cellSlice {
            val cdata = loadTlb(vmControlDataCodec)
            val next = loadRef {
                loadTlb(vmContCodec)
            }
            VmCont.Envelope(cdata, next)
        }
    }

    private class VmContQuitTlbConstructor : TlbConstructor<VmCont.Quit>(
        schema = "vmc_quit\$1000 exit_code:int32 = VmCont;"
    ) {
        override fun storeTlb(
            cellBuilder: CellBuilder, value: VmCont.Quit
        ) = cellBuilder {
            storeInt(value.exitCode, 32)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): VmCont.Quit = cellSlice {
            val exitCode = loadInt(32).toInt()
            VmCont.Quit(exitCode)
        }
    }

    private class VmContQuitExcTlbConstructor : TlbConstructor<VmCont.QuitExc>(
        schema = "vmc_quit_exc\$1001 = VmCont;"
    ) {
        override fun storeTlb(
            cellBuilder: CellBuilder, value: VmCont.QuitExc
        ) {
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): VmCont.QuitExc = VmCont.QuitExc
    }

    private class VmContRepeatTlbConstructor : TlbConstructor<VmCont.Repeat>(
        schema = "vmc_repeat\$10100 count:uint63 body:^VmCont after:^VmCont = VmCont;"
    ) {
        private val vmContCodec by lazy {
            VmCont.tlbCodec()
        }

        override fun storeTlb(
            cellBuilder: CellBuilder, value: VmCont.Repeat
        ) = cellBuilder {
            storeUInt(value.count, 63)
            storeRef {
                storeTlb(vmContCodec, value.body)
            }
            storeRef {
                storeTlb(vmContCodec, value.after)
            }
        }

        override fun loadTlb(
            cellSlice: CellSlice
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

    private class VmContUntilTlbConstructor : TlbConstructor<VmCont.Until>(
        schema = "vmc_until\$110000 body:^VmCont after:^VmCont = VmCont;"
    ) {
        private val vmContCodec by lazy {
            VmCont.tlbCodec()
        }

        override fun storeTlb(
            cellBuilder: CellBuilder, value: VmCont.Until
        ) = cellBuilder {
            storeRef {
                storeTlb(vmContCodec, value.body)
            }
            storeRef {
                storeTlb(vmContCodec, value.after)
            }
        }

        override fun loadTlb(
            cellSlice: CellSlice
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

    private class VmContAgainTlbConstructor : TlbConstructor<VmCont.Again>(
        schema = "vmc_again\$110001 body:^VmCont = VmCont;"
    ) {
        private val vmContCodec by lazy {
            VmCont.tlbCodec()
        }

        override fun storeTlb(
            cellBuilder: CellBuilder, value: VmCont.Again
        ) = cellBuilder {
            storeRef {
                storeTlb(vmContCodec, value.body)
            }
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): VmCont.Again = cellSlice {
            val body = loadRef {
                loadTlb(vmContCodec)
            }
            VmCont.Again(body)
        }
    }

    private class VmContWhileCondTlbConstructor : TlbConstructor<VmCont.WhileCond>(
        schema = "vmc_while_cond\$110010 cond:^VmCont body:^VmCont after:^VmCont = VmCont;"
    ) {
        private val vmContCodec by lazy {
            VmCont.tlbCodec()
        }

        override fun storeTlb(
            cellBuilder: CellBuilder, value: VmCont.WhileCond
        ) = cellBuilder {
            storeRef {
                storeTlb(vmContCodec, value.cond)
            }
            storeRef {
                storeTlb(vmContCodec, value.body)
            }
            storeRef {
                storeTlb(vmContCodec, value.after)
            }
        }

        override fun loadTlb(
            cellSlice: CellSlice
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

    private class VmContWhileBodyTlbConstructor : TlbConstructor<VmCont.WhileBody>(
        schema = "vmc_while_body\$110011 cond:^VmCont body:^VmCont after:^VmCont = VmCont;"
    ) {
        private val vmContCodec by lazy {
            VmCont.tlbCodec()
        }

        override fun storeTlb(
            cellBuilder: CellBuilder, value: VmCont.WhileBody
        ) = cellBuilder {
            storeRef {
                storeTlb(vmContCodec, value.cond)
            }
            storeRef {
                storeTlb(vmContCodec, value.body)
            }
            storeRef {
                storeTlb(vmContCodec, value.after)
            }
        }

        override fun loadTlb(
            cellSlice: CellSlice
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

    private class VmContPushIntTlbConstructor : TlbConstructor<VmCont.PushInt>(
        schema = "vmc_pushint\$1111 value:int32 next:^VmCont = VmCont;"
    ) {
        private val vmContCodec by lazy {
            VmCont.tlbCodec()
        }

        override fun storeTlb(
            cellBuilder: CellBuilder, value: VmCont.PushInt
        ) = cellBuilder {
            storeInt(value.value, 32)
            storeRef {
                storeTlb(vmContCodec, value.next)
            }
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): VmCont.PushInt = cellSlice {
            val value = loadInt(32).toInt()
            val next = loadTlb(vmContCodec)
            VmCont.PushInt(value, next)
        }
    }
}
