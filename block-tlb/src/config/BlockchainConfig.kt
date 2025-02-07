package org.ton.kotlin.config

import kotlinx.io.bytestring.ByteString

/**
 * Blockchain config.
 */
public data class BlockchainConfig(
    /**
     * Configuration contract address.
     */
    public val address: ByteString,

    /**
     * Configuration parameters.
     */
    public val params: BlockchainConfigParams
)