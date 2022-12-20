package org.ton.lite.api

import org.ton.lite.api.liteserver.*
import org.ton.lite.api.liteserver.functions.*

public interface LiteApi {
    public suspend operator fun invoke(function: LiteServerGetMasterchainInfo): LiteServerMasterchainInfo
    public suspend operator fun invoke(function: LiteServerGetMasterchainInfoExt): LiteServerMasterchainInfoExt
    public suspend operator fun invoke(function: LiteServerGetTime): LiteServerCurrentTime
    public suspend operator fun invoke(function: LiteServerGetVersion): LiteServerVersion
    public suspend operator fun invoke(function: LiteServerGetBlock): LiteServerBlockData
    public suspend operator fun invoke(function: LiteServerGetState): LiteServerBlockState
    public suspend operator fun invoke(function: LiteServerGetBlockHeader): LiteServerBlockHeader
    public suspend operator fun invoke(function: LiteServerSendMessage): LiteServerSendMsgStatus
    public suspend operator fun invoke(function: LiteServerGetAccountState): LiteServerAccountState
    public suspend operator fun invoke(function: LiteServerRunSmcMethod): LiteServerRunMethodResult
    public suspend operator fun invoke(function: LiteServerGetShardInfo): LiteServerShardInfo
    public suspend operator fun invoke(function: LiteServerGetOneTransaction): LiteServerTransactionInfo
    public suspend operator fun invoke(function: LiteServerGetAllShardsInfo): LiteServerAllShardsInfo
    public suspend operator fun invoke(function: LiteServerGetTransactions): LiteServerTransactionList
    public suspend operator fun invoke(function: LiteServerLookupBlock): LiteServerBlockHeader
    public suspend operator fun invoke(function: LiteServerListBlockTransactions): LiteServerBlockTransactions
    public suspend operator fun invoke(function: LiteServerGetBlockProof): LiteServerPartialBlockProof
    public suspend operator fun invoke(function: LiteServerGetConfigAll): LiteServerConfigInfo
    public suspend operator fun invoke(function: LiteServerGetConfigParams): LiteServerConfigInfo
    public suspend operator fun invoke(function: LiteServerGetValidatorStats): LiteServerValidatorStats
}
