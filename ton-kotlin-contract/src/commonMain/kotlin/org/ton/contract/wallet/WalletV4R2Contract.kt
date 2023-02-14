package org.ton.contract.wallet

import kotlinx.coroutines.coroutineScope
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.ton.api.pk.PrivateKeyEd25519
import org.ton.api.pub.PublicKeyEd25519
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.bitstring.BitString
import org.ton.block.*
import org.ton.boc.BagOfCells
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.contract.SmartContract
import org.ton.contract.wallet.WalletContract.Companion.DEFAULT_WALLET_ID
import org.ton.crypto.base64
import org.ton.lite.api.LiteApi
import org.ton.lite.api.liteserver.LiteServerAccountId
import org.ton.lite.client.LiteClient
import org.ton.tlb.CellRef
import org.ton.tlb.constructor.AnyTlbConstructor
import org.ton.tlb.storeRef
import kotlin.jvm.JvmField
import kotlin.jvm.JvmStatic
import kotlin.time.Duration.Companion.seconds

public class WalletV4R2Contract private constructor(
    override val address: MsgAddressInt,
    public override val state: AccountState
) : WalletContract<Cell> {
    public constructor(
        workchain: Int,
        init: StateInit
    ) : this(SmartContract.address(workchain, init), AccountUninit)

    public constructor(
        workchain: Int,
        publicKey: PublicKeyEd25519,
    ) : this(workchain, createStateInit(publicKey, DEFAULT_WALLET_ID + workchain))

    public constructor(
        accountInfo: AccountInfo
    ) : this(accountInfo.addr, accountInfo.storage.state)

    override fun loadData(): Cell? = data

    public fun getSeqno(): Int = requireNotNull(data).beginParse().run {
        preloadInt(32).toInt()
    }

    public fun getSubWalletId(): Int = requireNotNull(data).beginParse().run {
        skipBits(32)
        preloadInt(32).toInt()
    }

    public fun getPublicKey(): PublicKeyEd25519 = requireNotNull(data).beginParse().run {
        skipBits(64)
        PublicKeyEd25519(loadBits256())
    }

    public suspend fun transfer(
        liteApi: LiteApi,
        privateKey: PrivateKeyEd25519,
        vararg transfers: WalletTransfer
    ): Unit = transfer(liteApi, privateKey, Clock.System.now() + 60.seconds, *transfers)

    public suspend fun transfer(
        liteApi: LiteApi,
        privateKey: PrivateKeyEd25519,
        validUntil: Instant,
        vararg transfers: WalletTransfer
    ): Unit = coroutineScope {
        val seqno = getSeqno()
        val walletId = getSubWalletId()
        val message = createTransferMessage(
            address = address,
            stateInit = if (state !is AccountActive) createStateInit(privateKey.publicKey(), walletId) else null,
            privateKey = privateKey,
            validUntil = validUntil.epochSeconds.toInt(),
            walletId = walletId,
            seqno = seqno,
            transfers = transfers
        )
        sendExternalMessage(liteApi, AnyTlbConstructor, message)
    }

    private companion object {
        @JvmField
        val CODE =
            BagOfCells(base64("te6cckECFAEAAtQAART/APSkE/S88sgLAQIBIAIDAgFIBAUE+PKDCNcYINMf0x/THwL4I7vyZO1E0NMf0x/T//QE0VFDuvKhUVG68qIF+QFUEGT5EPKj+AAkpMjLH1JAyx9SMMv/UhD0AMntVPgPAdMHIcAAn2xRkyDXSpbTB9QC+wDoMOAhwAHjACHAAuMAAcADkTDjDQOkyMsfEssfy/8QERITAubQAdDTAyFxsJJfBOAi10nBIJJfBOAC0x8hghBwbHVnvSKCEGRzdHK9sJJfBeAD+kAwIPpEAcjKB8v/ydDtRNCBAUDXIfQEMFyBAQj0Cm+hMbOSXwfgBdM/yCWCEHBsdWe6kjgw4w0DghBkc3RyupJfBuMNBgcCASAICQB4AfoA9AQw+CdvIjBQCqEhvvLgUIIQcGx1Z4MesXCAGFAEywUmzxZY+gIZ9ADLaRfLH1Jgyz8gyYBA+wAGAIpQBIEBCPRZMO1E0IEBQNcgyAHPFvQAye1UAXKwjiOCEGRzdHKDHrFwgBhQBcsFUAPPFiP6AhPLassfyz/JgED7AJJfA+ICASAKCwBZvSQrb2omhAgKBrkPoCGEcNQICEekk30pkQzmkD6f+YN4EoAbeBAUiYcVnzGEAgFYDA0AEbjJftRNDXCx+AA9sp37UTQgQFA1yH0BDACyMoHy//J0AGBAQj0Cm+hMYAIBIA4PABmtznaiaEAga5Drhf/AABmvHfaiaEAQa5DrhY/AAG7SB/oA1NQi+QAFyMoHFcv/ydB3dIAYyMsFywIizxZQBfoCFMtrEszMyXP7AMhAFIEBCPRR8qcCAHCBAQjXGPoA0z/IVCBHgQEI9FHyp4IQbm90ZXB0gBjIywXLAlAGzxZQBPoCFMtqEssfyz/Jc/sAAgBsgQEI1xj6ANM/MFIkgQEI9Fnyp4IQZHN0cnB0gBjIywXLAlAFzxZQA/oCE8tqyx8Syz/Jc/sAAAr0AMntVGliJeU=")).first()

        @JvmField
        val OP_TRANSFER = 0

        @JvmStatic
        suspend fun loadContract(liteClient: LiteClient, address: AddrStd): WalletV4R2Contract? {
            val blockId = liteClient.getLastBlockId()
            return loadContract(liteClient, blockId, address)
        }

        @JvmStatic
        suspend fun loadContract(liteClient: LiteClient, blockId: TonNodeBlockIdExt, address: AddrStd): WalletV4R2Contract? {
            val accountInfo = liteClient.getAccount(LiteServerAccountId(address.workchainId, address.address), blockId) ?: return null
            return WalletV4R2Contract(accountInfo)
        }

        @JvmStatic
        fun createTransferMessage(
            address: MsgAddressInt,
            stateInit: StateInit?,
            privateKey: PrivateKeyEd25519,
            walletId: Int,
            validUntil: Int,
            seqno: Int,
            vararg transfers: WalletTransfer
        ): Message<Cell> {
            val info = ExtInMsgInfo(
                src = AddrNone,
                dest = address,
                importFee = Coins()
            )
            val maybeStateInit =
                Maybe.of(stateInit?.let { Either.of<StateInit, CellRef<StateInit>>(null, CellRef(it)) })
            val transferBody = createTransferMessageBody(
                privateKey,
                walletId,
                validUntil,
                seqno,
                *transfers
            )
            val body = Either.of<Cell, CellRef<Cell>>(null, CellRef(transferBody))
            return Message(
                info = info,
                init = maybeStateInit,
                body = body
            )
        }

        @JvmStatic
        fun createStateInit(
            publicKey: PublicKeyEd25519,
            walletId: Int,
        ): StateInit {
            val data = CellBuilder.createCell {
                storeUInt(0, 32) // seqno
                storeUInt(walletId, 32)
                storeBits(publicKey.key)
                storeBit(false) // plugins
            }
            return StateInit(
                code = CODE,
                data = data
            )
        }

        private fun createTransferMessageBody(
            privateKey: PrivateKeyEd25519,
            walletId: Int,
            validUntil: Int,
            seqno: Int,
            vararg gifts: WalletTransfer
        ): Cell {
            val unsignedBody = CellBuilder.createCell {
                storeUInt(walletId, 32)
                storeUInt(validUntil, 32)
                storeUInt(seqno, 32)
                storeUInt(OP_TRANSFER, 8) // op
                for (gift in gifts) {
                    var sendMode = 3
                    if (gift.sendMode > -1) {
                        sendMode = gift.sendMode
                    }
                    val intMsg = CellRef(createIntMsg(gift))

                    storeUInt(sendMode, 8)
                    storeRef(MessageRelaxed.tlbCodec(AnyTlbConstructor), intMsg)
                }
            }
            val signature = BitString(privateKey.sign(unsignedBody.hash()))

            return CellBuilder.createCell {
                storeBits(signature)
                storeBits(unsignedBody.bits)
                storeRefs(unsignedBody.refs)
            }
        }

        private fun createIntMsg(gift: WalletTransfer): MessageRelaxed<Cell> {
            val info = CommonMsgInfoRelaxed.IntMsgInfoRelaxed(
                ihrDisabled = true,
                bounce = gift.bounceable,
                bounced = false,
                src = AddrNone,
                dest = gift.destination,
                value = gift.coins,
                ihrFee = Coins(),
                fwdFee = Coins(),
                createdLt = 0u,
                createdAt = 0u
            )
            val init = Maybe.of(gift.stateInit?.let {
                Either.of<StateInit, CellRef<StateInit>>(null, CellRef(it))
            })
            val body = if (gift.body == null) {
                Either.of<Cell, CellRef<Cell>>(Cell.empty(), null)
            } else {
                Either.of<Cell, CellRef<Cell>>(null, CellRef(gift.body))
            }

            return MessageRelaxed(
                info = info,
                init = init,
                body = body,
            )
        }
    }
}
