package org.ton.kotlin.shard

public data class ShardIdent(
    val workchain: Int,
    val prefix: ULong
) {
    public constructor(workchain: Int) : this(workchain, prefix = PREFIX_ROOT)

    val prefixLength: Int
        get() = when (prefix) {
            0uL -> 64
            else -> 63 - prefix.countTrailingZeroBits()
        }

    /**
     * `true` if this shard is a masterchain shard.
     */
    val isMasterchain: Boolean get() = workchain == MASTERCHAIN.workchain

    /**
     * `true` if this shard could not be merged further.
     */
    val isRoot: Boolean get() = prefix == PREFIX_ROOT

    /**
     * Whether the shard depth is in the possible range.
     */
    val canSplit: Boolean get() = prefixLength < MAX_SPLIT_DEPTH

    /**
     * Parent shard of the current shard.
     */
    public fun merge(): ShardIdent {
        if (isRoot) return this
        val tag = prefixTag
        return ShardIdent(
            workchain = workchain,
            prefix = (prefix - tag) or (tag shl 1)
        )
    }

    /**
     * Splits the current shard into two children.
     */
    public fun split(): Pair<ShardIdent, ShardIdent> {
        if (!canSplit) {
            throw IllegalArgumentException("Shard identifier can't be split")
        }
        val tag = prefixTag shr 1
        val left = ShardIdent(workchain, prefix - tag)
        val right = ShardIdent(workchain, prefix + tag)
        return Pair(left, right)
    }

    public operator fun contains(other: ShardIdent): Boolean {
        if (workchain != other.workchain) return false
        val parent = prefix
        val child = other.prefix
        val x = lowerBits64(parent)
        return ((parent xor child) and (bitsNegative64(x) shl 1)) == 0uL
    }

    internal inline val prefixTagMask: ULong get() = prefix.inv() + 1uL
    internal inline val prefixTag: ULong get() = prefix and prefixTagMask


    override fun toString(): String = "$workchain:${prefix.toHexString()}"

    public companion object {
        /**
         * The prefix for the root shard.
         */
        public const val PREFIX_ROOT: ULong = 0x8000000000000000uL

        /**
         * Max possible shard split depth.
         */
        public const val MAX_SPLIT_DEPTH: Int = 60

        /**
         * Masterchain shard ident.
         */
        public val MASTERCHAIN: ShardIdent = ShardIdent(workchain = -1)

        /**
         * Base workchain shard ident.
         */
        public val BASECHAIN: ShardIdent = ShardIdent(workchain = 0)
    }
}

@Suppress("NOTHING_TO_INLINE")
private inline fun lowerBits64(x: ULong) = x and bitsNegative64(x)

@Suppress("NOTHING_TO_INLINE")
private inline fun bitsNegative64(x: ULong) = x.inv() + 1uL

//private object ShardIdentTlbConstructor : TlbConstructor<ShardIdent>(
//    schema = "shard_ident\$00 shard_pfx_bits:(#<= 60) " +
//            "workchain_id:int32 shard_prefix:uint64 = ShardIdent;"
//) {
//    override fun storeTlb(
//        cellBuilder: CellBuilder,
//        value: ShardIdent
//    ) = cellBuilder {
//        val prefixLength = value.prefixLength
//        val prefixWithoutTag = value.prefix - value.prefixTag
//        storeUIntLeq(prefixLength, 60)
//        storeInt(value.workchain, 32)
//        storeUInt(prefixWithoutTag.toLong(), 64)
//    }
//
//    override fun loadTlb(
//        cellSlice: CellSlice
//    ): ShardIdent = cellSlice {
//        val prefixLen = loadUIntLeq(60).toInt()
//        val workchain = loadInt(32)
//        val prefixWithoutTag = loadULong()
//        val tag = 1uL shl (63 - prefixLen)
//        val prefix = (prefixWithoutTag and (tag.inv() + 1uL)) or tag
//        ShardIdent(workchain, prefix)
//    }
//}
