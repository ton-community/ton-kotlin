package org.ton.block

import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbLoader
import org.ton.tlb.TlbObject
import org.ton.tlb.providers.TlbCombinatorProvider

@Serializable
public sealed interface MsgAddress : TlbObject {
    public companion object : TlbCombinatorProvider<MsgAddress> by MsgAddressTlbCombinator
}

private object MsgAddressTlbCombinator : TlbCombinator<MsgAddress>(
    MsgAddress::class,
    MsgAddressInt::class to MsgAddressInt.tlbCodec(),
    MsgAddressExt::class to MsgAddressExt.tlbCodec(),
) {
    override fun findTlbLoaderOrNull(bitString: BitString): TlbLoader<out MsgAddress>? {
        return if (bitString.size >= 2) {
            if (bitString[0]) { // 1
                if (bitString[1]) { // 11
                    AddrVar.tlbCodec()
                } else { // 10
                    AddrStd.tlbCodec()
                }
            } else { // 0
                if (bitString[1]) { // 01
                    AddrExtern.tlbConstructor()
                } else { // 00
                    AddrNone.tlbConstructor()
                }
            }
        } else {
            null
        }
    }
}
