package org.ton.tonlib.functions

import org.ton.tonlib.types.FullAccountState
import org.ton.tonlib.types.OptionsInfo
import org.ton.tonlib.types.Sync
import org.ton.tonlib.types.TonBlockIdExt

internal interface TonLibApi {
    suspend fun invoke(query: Init): OptionsInfo
    suspend operator fun invoke(sync: Sync): TonBlockIdExt

    suspend fun getAccountState(accountAddress: String): FullAccountState
}
