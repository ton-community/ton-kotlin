//package ton.tlb.types
//
//import kotlinx.serialization.Serializable
//import ton.bitstring.BitString
//
//sealed interface MsgAddressExt
//
//@Serializable
//object AddrNone : MsgAddressExt
//
//@Serializable
//data class AddrExtern(
//    val len: Int,
//    val external_address: BitString,
//) : MsgAddressExt
//
//sealed interface Anycast
//
//@Serializable
//data class AnycastInfo(
//    val depth: Int,
//    val rewrite_pfx: BitString,
//)