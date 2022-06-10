@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@JsonClassDiscriminator("@type")
@Serializable
sealed interface VmCont {


    @SerialName("vmc_quit")
    @Serializable
    data class Quit(
        @SerialName("exit_code")
        val exitCode: Int
    ) : VmCont

    @SerialName("vmc_quit_exc")
    @Serializable
    object QuitExc : VmCont

    @SerialName("vmc_repeat")
    @Serializable
    data class Repeat(
        val count: Long,
        val body: VmCont,
        val after: VmCont
    ) : VmCont

    @SerialName("vmc_until")
    @Serializable
    data class Until(
        val body: VmCont,
        val after: VmCont
    ) : VmCont

    @SerialName("vmc_again")
    @Serializable
    data class Again(
        val body: VmCont
    ) : VmCont

    @SerialName("vmc_while_cond")
    @Serializable
    data class WhileCond(
        val cond: VmCont,
        val body: VmCont,
        val after: VmCont
    ) : VmCont

    @SerialName("vmc_while_body")
    @Serializable
    data class WhileBody(
        val cond: VmCont,
        val body: VmCont,
        val after: VmCont
    ) : VmCont

    @SerialName("vmc_pushint")
    @Serializable
    data class PushInt(
        val value: Int,
        val next: VmCont
    ) : VmCont

    companion object {
        @JvmStatic
        fun tlbCodec(): TlbCombinator<VmCont> = VmContTlbCombinator()
    }
}

private class VmContTlbCombinator : TlbCombinator<VmCont>() {

    private val stdConstructor by lazy {
        VmContStd.tlbCodec()
    }
    private val envelopeConstructor by lazy {
        VmContEnvelope.tlbCodec()
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
        is VmContStd -> stdConstructor
        is VmContEnvelope -> envelopeConstructor
        is VmCont.Quit -> quitConstructor
        is VmCont.QuitExc -> quitExcConstructor
        is VmCont.Repeat -> repeatConstructor
        is VmCont.Until -> untilConstructor
        is VmCont.Again -> againConstructor
        is VmCont.WhileBody -> whileBodyConstructor
        is VmCont.WhileCond -> whileCondConstructor
        is VmCont.PushInt -> pushIntConstructor
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
