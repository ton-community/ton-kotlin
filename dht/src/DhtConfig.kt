package org.ton.dht

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * The `k` parameter of the TON DHT.
 *
 * This parameter determines:
 *
 *   1) The (fixed) maximum number of nodes in a bucket.
 *   2) The number of nodes to return in response to a query.
 */
public const val K_VALUE: Int = 10

/**
 * The `a` parameter of the TON DHT.
 *
 * This parameter determines the default parallelism for iterative queries,
 * i.e., the allowed number of in-flight requests that an iterative query is
 * waiting for at a particular time while it continues to search for the target.
 */
public const val ALPHA_VALUE: Int = 3

public data class DhtConfig(
    val query: QueryConfig
)

public data class QueryConfig(
    /**
     * Timeout for a single query
     */
    val timeout: Duration = 60.seconds,

    /**
     * The replication factor for DHT.
     *
     * The `k` parameter of the TON DHT, which determines the number of nodes
     * a value is replicated to.
     *
     * The default value is [K_VALUE].
     */
    val replicationFactor: Int = K_VALUE,

    /**
     * The parallelism for iterative queries.
     *
     * The `a` parameter of the TON DHT, which determines the number of nodes
     * to query in parallel during iterative queries.
     *
     * The default value is [ALPHA_VALUE].
     */
    val parallelism: Int = ALPHA_VALUE,

    /**
     * Replication interval for stored values.
     *
     * Periodic replication of stored values ensures that the values
     * are always replicated to the available nodes closest to the key
     * in the context of DHT topology changes (i.e., nodes joining and leaving).
     * Replication does not prolong the lifetime of the value (for otherwise
     * it would live forever regardless of the original TTL). The expiry of
     * a record is only extended through re-publication.
     *
     * This interval should be significantly smaller than the publication interval,
     * to ensure persistence between re-publications.
     *
     * `null` means that stored values are never re-published.
     */
    val replicationInterval: Duration? = 10.seconds,

    /**
     * Publication interval for stored values.
     *
     * Values persist in the DHT until they expire. By default,
     * published values are re-published in regular intervals for
     * as long as the record exists in the local storage of the original
     * publisher, thereby extending the records lifetime.
     *
     * This interval should be significantly shorter than the TTL of the record,
     * to ensure that the record is re-published before it expires.
     *
     * `null` means that stored values are never re-published.
     */
    val publicationInterval: Duration? = 60.seconds
) {
    init {
        require(replicationFactor > 0) { "Replication factor must be positive" }
    }
}