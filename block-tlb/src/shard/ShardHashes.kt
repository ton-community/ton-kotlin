package org.ton.kotlin.shard

import org.ton.kotlin.cell.Cell
import org.ton.kotlin.dict.Dictionary

/**
 * A tree of the most recent descriptions for all currently existing shards for all workchains except the masterchain.
 */
public class ShardHashes(
    public val dict: Dictionary<Int, Cell>
)