package org.ton.kotlin.examples.contract

import contract.BaseWalletContract
import org.ton.api.pk.PrivateKey
import org.ton.api.pub.PublicKeyEd25519
import org.ton.bitstring.BitString
import org.ton.block.*
import org.ton.boc.BagOfCells
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.contract.wallet.WalletTransfer
import org.ton.kotlin.examples.provider.Provider
import org.ton.kotlin.message.MessageLayout
import org.ton.tlb.CellRef
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbStorer
import org.ton.tlb.constructor.AnyTlbConstructor
import org.ton.tlb.storeRef

open class WalletV1R3Contract(
    workchain: Int = DEFAULT_WORKCHAIN,
    publicKey: PublicKeyEd25519,
    provider: Provider,
) : BaseWalletContract(workchain, publicKey, provider) {
    override val maxMessages: Int = 4

    override fun initDataCell(): Cell {
        return CellBuilder.Companion.createCell {
            storeUInt(0, 32)
            storeBytes(publicKey.key.toByteArray())
        }
    }

    override fun initCodeCell(): Cell = CODE

    override suspend fun getSeqno(accountInfo: AccountInfo): Int {
        val state = (accountInfo.storage.state as AccountActive).value
        val data = requireNotNull(state.data.value).value.beginParse()
        return data.loadUInt(32).toInt()
    }

    suspend fun transfer(privateKey: PrivateKey, seqno: Int, transfers: List<WalletTransfer>) {
        val message = WalletV1R3Message(seqno, transfers).sign(privateKey).toMessage(address)
        provider.sendMessage(SignedWalletV1R3Message, message)
    }

    open class WalletV1R3Message(
        override val seqno: Int,
        override val transfers: List<WalletTransfer>,
    ) : WalletMessage {
        constructor(seqno: Int, vararg transfers: WalletTransfer) : this(seqno, transfers.toList())

        init {
            require(transfers.size <= 4) { "Maximum number of transfers is 4" }
        }

        override fun sign(privateKey: PrivateKey): SignedWalletV1R3Message {
            val cell = CellBuilder().apply {
                storeTlb(this, this@WalletV1R3Message)
            }.build()
            val signature = BitString(privateKey.sign(cell.hash().toByteArray()))
            return SignedWalletV1R3Message(signature, seqno, transfers)
        }

        companion object : TlbStorer<WalletV1R3Message> {
            override fun storeTlb(
                builder: CellBuilder,
                value: WalletV1R3Message
            ) {
                builder.storeUInt(value.seqno.toUInt())
                for (gift in value.transfers) {
                    val intMsg = CellRef(gift.toMessageRelaxed())

                    builder.storeUInt(gift.sendMode, 8)
                    builder.storeRef(MessageRelaxed.Companion.tlbCodec(AnyTlbConstructor), intMsg)
                }
            }
        }
    }

    class SignedWalletV1R3Message(
        override val signature: BitString,
        seqno: Int,
        transfers: List<WalletTransfer>,
    ) : WalletV1R3Message(seqno, transfers), SignedWalletMessage {
        override fun sign(privateKey: PrivateKey): SignedWalletV1R3Message = this

        fun toMessage(
            address: MsgAddressInt,
            init: StateInit? = null,
            layout: MessageLayout? = null,
        ) = toMessage(null, address, init, layout)

        fun toMessage(
            source: AddrExtern?,
            address: MsgAddressInt,
            init: StateInit? = null,
            layout: MessageLayout? = null
        ): Message<SignedWalletV1R3Message> {
            val info = ExtInMsgInfo(source, address)
            return Message<SignedWalletV1R3Message>(
                info = info,
                init = init,
                body = this,
                layout = layout ?: MessageLayout.compute(
                    info = info,
                    init = init,
                    body = this,
                    bodyStorer = Companion
                )
            )
        }

        companion object : TlbCodec<SignedWalletV1R3Message> {
            override fun storeTlb(
                builder: CellBuilder,
                value: SignedWalletV1R3Message
            ) {
                builder.storeBitString(value.signature)
                WalletV1R3Message.storeTlb(builder, value)
            }

            override fun loadTlb(cellSlice: CellSlice): SignedWalletV1R3Message {
                TODO("Not yet implemented")
            }
        }
    }

    companion object {
        @OptIn(ExperimentalStdlibApi::class)
        @JvmField
        val CODE = BagOfCells(
            "B5EE9C7241010101005F0000BAFF0020DD2082014C97BA218201339CBAB19C71B0ED44D0D31FD70BFFE304E0A4F260810200D71820D70B1FED44D0D31FD3FFD15112BAF2A122F901541044F910F2A2F80001D31F3120D74A96D307D402FB00DED1A4C8CB1FCBFFC9ED54B5B86E42".hexToByteArray()
        ).first()
    }
}