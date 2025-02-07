@file:Suppress("OPT_IN_USAGE")

package org.ton.kotlin.message.envelope

/**
 * Next-hop address for a message.
 */
public sealed interface IntermediateAddr

public sealed interface NonRegularIntermediateAddr : IntermediateAddr {
    /**
     * Returns target workchain id if specified. Returns null if the same workchain is used.
     */
    public val workchain: Int

    /**
     * Returns the address prefix if specified
     */
    public val addressPrefix: Long
}

/**
 * Destination prefix length whithin the same workchain.
 *
 * ```tlb
 * interm_addr_regular$0 use_dest_bits:(#<= 96)
 *   = IntermediateAddress;
 * ```
 */
public data class IntermediateAddrRegular(
    /**
     * Destination address prefix length in bits.
     */
    val useDestBits: Int
) : IntermediateAddr {
    /**
     * Source address prefix length in bits.
     */
    val useSrcBits: Int get() = FULL_BITS - useDestBits

    public companion object {
        private const val FULL_BITS: Int = 96
    }
}

/**
 * Address prefix with a basic workchain id.
 *
 * ```tlb
 * interm_addr_simple$10 workchain_id:int8 addr_pfx:uint64
 *   = IntermediateAddress;
 */
public data class IntermediateAddrSimple(
    override val workchain: Int,
    override val addressPrefix: Long
) : NonRegularIntermediateAddr

/**
 * Address prefix with an extended workchain id.
 *
 * ```tlb
 * interm_addr_ext$11 workchain_id:int32 addr_pfx:uint64
 *   = IntermediateAddress;
 * ```
 */
public data class IntermediateAddrExt(
    override val workchain: Int,
    override val addressPrefix: Long
) : NonRegularIntermediateAddr