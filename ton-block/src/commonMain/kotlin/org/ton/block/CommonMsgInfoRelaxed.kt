@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.*

@JsonClassDiscriminator("@type")
@Serializable
sealed interface CommonMsgInfoRelaxed {
    @SerialName("int_msg_info")
    data class IntMsgInfo(
        @SerialName("ihr_disabled")
        val ihrDisabled: Boolean,
        val bounce: Boolean,
        val bounced: Boolean,
        val src: MsgAddress,
        val dest: MsgAddressInt,
        val value: CurrencyCollection,
        @SerialName("ihr_fee")
        val ihrFee: Coins = Coins(),
        @SerialName("fwd_fee")
        val fwdFee: Coins = Coins(),
        @SerialName("created_lt")
        val createdLt: Long = 0,
        @SerialName("created_at")
        val createdAt: Int = 0
    ) : CommonMsgInfoRelaxed

    @SerialName("ext_out_msg_info")
    data class ExtOutMsgInfo(
        val src: MsgAddress,
        val dest: MsgAddressExt,
        @SerialName("created_lt")
        val createdLt: Long,
        @SerialName("created_at")
        val createdAt: Int
    ) : CommonMsgInfoRelaxed

    companion object {
        fun tlbCodec(): TlbCodec<CommonMsgInfoRelaxed> = CommonMsgInfoRelaxedTlbCombinator()
    }
}

private class CommonMsgInfoRelaxedTlbCombinator : TlbCombinator<CommonMsgInfoRelaxed>() {
    private val intMsgInfoConstructor by lazy {
        IntMsgInfoTlbConstructor()
    }
    private val extOutMsgInfoConstructor by lazy {
        ExtOutMsgInfoTlbConstructor()
    }

    override val constructors: List<TlbConstructor<out CommonMsgInfoRelaxed>> by lazy {
        listOf(intMsgInfoConstructor, extOutMsgInfoConstructor)
    }

    override fun getConstructor(value: CommonMsgInfoRelaxed): TlbConstructor<out CommonMsgInfoRelaxed> = when (value) {
        is CommonMsgInfoRelaxed.IntMsgInfo -> intMsgInfoConstructor
        is CommonMsgInfoRelaxed.ExtOutMsgInfo -> extOutMsgInfoConstructor
    }

    private class IntMsgInfoTlbConstructor : TlbConstructor<CommonMsgInfoRelaxed.IntMsgInfo>(
        schema = "int_msg_info\$0 ihr_disabled:Bool bounce:Bool bounced:Bool" +
                " src:MsgAddress dest:MsgAddressInt" +
                " value:CurrencyCollection ihr_fee:Coins fwd_fee:Coins" +
                " created_lt:uint64 created_at:uint32 = CommonMsgInfoRelaxed;"
    ) {
        private val msgAddressCodec by lazy {
            MsgAddress.tlbCodec()
        }
        private val msgAddressIntCodec by lazy {
            MsgAddressInt.tlbCodec()
        }
        private val currencyCollectionCodec by lazy {
            CurrencyCollection.tlbCodec()
        }
        private val coinsCodec by lazy {
            Coins.tlbCodec()
        }

        override fun storeTlb(
            cellBuilder: CellBuilder, value: CommonMsgInfoRelaxed.IntMsgInfo
        ) = cellBuilder {
            storeBit(value.ihrDisabled)
            storeBit(value.bounce)
            storeBit(value.bounced)
            storeTlb(msgAddressCodec, value.src)
            storeTlb(msgAddressIntCodec, value.dest)
            storeTlb(currencyCollectionCodec, value.value)
            storeTlb(coinsCodec, value.ihrFee)
            storeTlb(coinsCodec, value.fwdFee)
            storeUInt(value.createdLt, 64)
            storeUInt(value.createdAt, 32)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): CommonMsgInfoRelaxed.IntMsgInfo = cellSlice {
            val ihrDisabled = loadBit()
            val bounce = loadBit()
            val bounced = loadBit()
            val src = loadTlb(msgAddressCodec)
            val dest = loadTlb(msgAddressIntCodec)
            val value = loadTlb(currencyCollectionCodec)
            val ihrFee = loadTlb(coinsCodec)
            val fwdFee = loadTlb(coinsCodec)
            val createdLt = loadUInt(64).toLong()
            val createdAt = loadUInt(32).toInt()
            CommonMsgInfoRelaxed.IntMsgInfo(
                ihrDisabled, bounce, bounced, src, dest, value, ihrFee, fwdFee, createdLt, createdAt
            )
        }
    }

    private class ExtOutMsgInfoTlbConstructor : TlbConstructor<CommonMsgInfoRelaxed.ExtOutMsgInfo>(
        schema = "ext_out_msg_info\$11 src:MsgAddress dest:MsgAddressExt" +
                " created_lt:uint64 created_at:uint32 = CommonMsgInfoRelaxed;"
    ) {
        private val msgAddressCodec by lazy {
            MsgAddress.tlbCodec()
        }
        private val msgAddressExtCodec by lazy {
            MsgAddressExt.tlbCodec()
        }

        override fun storeTlb(
            cellBuilder: CellBuilder, value: CommonMsgInfoRelaxed.ExtOutMsgInfo
        ) = cellBuilder {
            storeTlb(msgAddressCodec, value.src)
            storeTlb(msgAddressExtCodec, value.dest)
            storeUInt(value.createdLt, 64)
            storeUInt(value.createdAt, 32)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): CommonMsgInfoRelaxed.ExtOutMsgInfo = cellSlice {
            val src = loadTlb(msgAddressCodec)
            val dest = loadTlb(msgAddressExtCodec)
            val createdLt = loadUInt(64).toLong()
            val createdAt = loadUInt(32).toInt()
            CommonMsgInfoRelaxed.ExtOutMsgInfo(src, dest, createdLt, createdAt)
        }
    }
}
