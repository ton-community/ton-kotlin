package org.ton.block.shard

import org.ton.block.account.ShardAccount
import org.ton.hashmap.HashmapAugE

/**
 * A dictionary of account states.
 */
public typealias ShardAccounts = HashmapAugE<ShardAccount, DepthBalanceInfo>