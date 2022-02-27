package ton.tlb.types

import kotlinx.serialization.Serializable
import ton.bitstring.BitString
import ton.tlb.TlbBits
import ton.tlb.TlbConstructorPrefix
import ton.tlb.TlbNumber

@Serializable
sealed class MsgAddressExt {
    @Serializable
    @TlbConstructorPrefix(bitPrefix = [false, false])
    object AddrNone : MsgAddressExt()

    @Serializable
    @TlbConstructorPrefix(bitPrefix = [false, true])
    data class AddrExtern(
        @TlbNumber(bitSize = 9)
        val len: Int,
        @TlbBits(lengthField = "len")
        val external_address: BitString,
    ) : MsgAddressExt()
}

@Serializable
sealed class Anycast {
    @Serializable
    @TlbConstructorPrefix(bitPrefix = [])
    data class AnycastInfo(
        @TlbNumber(30)
        val depth: Int,
        @TlbBits(lengthField = "depth")
        val rewrite_pfx: BitString,
    ) : Anycast()
}