package org.ton.kotlin.block

/**
 * Software info.
 */
public data class GlobalVersion(
    /**
     * Software version.
     */
    val version: Int,

    /**
     * Software capability flags.
     */
    val capabilities: GlobalCapabilities
)