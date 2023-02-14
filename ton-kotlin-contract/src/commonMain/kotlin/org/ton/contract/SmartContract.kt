package org.ton.contract

import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.block.*
import org.ton.boc.BagOfCells
import org.ton.cell.Cell
import org.ton.cell.buildCell
import org.ton.crypto.crc16
import org.ton.lite.api.LiteApi
import org.ton.lite.api.liteserver.LiteServerAccountId
import org.ton.lite.api.liteserver.functions.LiteServerGetAccountState
import org.ton.lite.api.liteserver.functions.LiteServerGetMasterchainInfo
import org.ton.lite.api.liteserver.functions.LiteServerRunSmcMethod
import org.ton.lite.api.liteserver.functions.LiteServerSendMessage
import org.ton.tlb.CellRef
import org.ton.tlb.TlbCodec
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.jvm.JvmStatic

public interface SmartContract<T : Any> {
    public val address: MsgAddressInt
    public val state: AccountState
    public val data: Cell? get() = (state as? AccountActive)?.value?.data?.value?.value
    public val code: Cell? get() = (state as? AccountActive)?.value?.code?.value?.value

    public fun loadData(): T?

    public suspend fun <X : Any> sendExternalMessage(liteApi: LiteApi, codec: TlbCodec<X>, message: Message<X>): Int =
        sendExternalMessage(liteApi, buildCell {
            storeTlb(Message.tlbCodec(codec), message)
        })

    public suspend fun sendExternalMessage(liteApi: LiteApi, message: Cell): Int =
        liteApi(LiteServerSendMessage(BagOfCells(message).toByteArray())).status

    public suspend fun runGetMethod(liteApi: LiteApi, method: String): SmartContractAnswer =
        runGetMethod(liteApi) {
            this.method = method
        }

    public suspend fun runGetMethod(liteApi: LiteApi, blockId: TonNodeBlockIdExt, method: String): SmartContractAnswer =
        runGetMethod(liteApi, blockId) {
            this.method = method
        }

    public suspend fun runGetMethod(liteApi: LiteApi, query: SmartContractQuery): SmartContractAnswer =
        runGetMethod(liteApi, liteApi(LiteServerGetMasterchainInfo).last, query)

    public suspend fun runGetMethod(
        liteApi: LiteApi,
        blockId: TonNodeBlockIdExt,
        query: SmartContractQuery
    ): SmartContractAnswer {
        val address =
            requireNotNull(address as? AddrStd) { throw UnsupportedOperationException("expected AddrStd, actual: $address") }
        val result = liteApi(
            LiteServerRunSmcMethod(
                mode = 4,
                id = blockId,
                account = LiteServerAccountId(address.workchainId, address.address),
                methodId = query.methodId,
                params = LiteServerRunSmcMethod.params(query.stack)
            )
        )
        return SmartContractAnswer(
            exitCode = result.exitCode,
            stack = result.result?.let {
                if (it.isNotEmpty()) { // TODO: research why it is empty sometimes (bug in lite server?)
                    VmStack.loadTlb(BagOfCells(it).first())
                } else null
            }
        )
    }

    public companion object {
        @JvmStatic
        public fun address(workchain: Int, stateInit: StateInit): AddrStd =
            AddrStd(workchain, buildCell { storeTlb(StateInit, stateInit) }.hash())

        @JvmStatic
        public suspend fun getAccountInfo(
            liteApi: LiteApi,
            blockId: TonNodeBlockIdExt,
            address: AddrStd
        ): AccountInfo? {
            val accountState =
                liteApi(LiteServerGetAccountState(blockId, LiteServerAccountId(address.workchainId, address.address)))
            val stateBoc = BagOfCells(accountState.state)
            val account = stateBoc.first().parse { loadTlb(Account) }
            return account as? AccountInfo
        }
    }
}


public data class SmartContractAnswer(
    val exitCode: Int,
    val stack: VmStack?
) {
    val isSuccess: Boolean get() = exitCode == 0
}

public data class SmartContractQuery(
    val methodId: Long,
    val stack: VmStack
)

@OptIn(ExperimentalContracts::class)
public inline fun SmartContractQuery(builderAction: SmartContractQueryBuilder.() -> Unit): SmartContractQuery {
    contract {
        callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
    }
    return SmartContractQueryBuilder().apply(builderAction).build()
}

public class SmartContractQueryBuilder {
    public var methodId: Long = 0
    public var stack: VmStack = VmStack(VmStackList())

    public var method: String
        get() = throw UnsupportedOperationException()
        set(value) {
            crc16(value).toLong() or 0x10000
        }

    public var params: List<VmStackValue>
        get() = stack.stack.toList()
        set(value) {
            params(value)
        }

    public fun params(vmStackList: VmStackList?): ByteArray =
        LiteServerRunSmcMethod.params(vmStack = VmStack(vmStackList ?: VmStackList()))

    public fun params(params: Iterable<VmStackValue>): ByteArray =
        params(vmStackList = VmStackList(params))

    public fun params(vararg params: VmStackValue): ByteArray =
        params(params.asIterable())

    public fun build(): SmartContractQuery = SmartContractQuery(methodId, stack)
}

@OptIn(ExperimentalContracts::class)
public suspend fun SmartContract<*>.runGetMethod(
    liteApi: LiteApi,
    builderAction: SmartContractQueryBuilder.() -> Unit
): SmartContractAnswer {
    contract {
        callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
    }
    return runGetMethod(liteApi, SmartContractQueryBuilder().apply(builderAction).build())
}

@OptIn(ExperimentalContracts::class)
public suspend fun SmartContract<*>.runGetMethod(
    liteApi: LiteApi,
    blockId: TonNodeBlockIdExt,
    builderAction: SmartContractQueryBuilder.() -> Unit
): SmartContractAnswer {
    contract {
        callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
    }
    return runGetMethod(liteApi, blockId, SmartContractQueryBuilder().apply(builderAction).build())
}
