package org.ton.kotlin.message.address

import kotlinx.io.bytestring.ByteString
import org.ton.kotlin.bitstring.BitString
import org.ton.kotlin.bitstring.toBitString

/**
 * Variable-length internal address.
 *
 * ```tlb
 * addr_var$11 anycast:(Maybe Anycast) addr_len:(## 9)
 *    workchain_id:int32 address:(bits addr_len) = MsgAddressInt;
 * ```
 */
public data class VarAddr(
    /**
     * Optional anycast info.
     */
    val anycast: Anycast?,

    /**
     * Workchain id (full range).
     */
    override val workchain: Int,

    /**
     * Variable-length address.
     */
    override val address: BitString,
) : IntAddr {
    public constructor(workchainId: Int, address: ByteArray) : this(null, workchainId, address.toBitString())
    public constructor(workchainId: Int, address: ByteString) : this(null, workchainId, address.toBitString())
    public constructor(workchainId: Int, address: BitString) : this(null, workchainId, address)

    override fun toAddrStd(): StdAddr = StdAddr(anycast, workchain, address)

    override fun rewriteAnycast(): VarAddr = VarAddr(
        workchain,
        anycast?.rewrite(address) ?: address,
    )
}

