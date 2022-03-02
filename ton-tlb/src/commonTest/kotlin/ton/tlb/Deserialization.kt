package ton.tlb

import ton.bitstring.BitString
import ton.tlb.types.AddrExtern
import ton.tlb.types.AddrNone
import ton.tlb.types.MsgAddressExt
import kotlin.test.Test
import kotlin.test.assertEquals

class Deserialization {

    @Test
    fun test() {
        val addrNone: MsgAddressExt = Tlb.decodeFromBitString(BitString(false, false))
        assertEquals(AddrNone, addrNone)
        val addrExtern: MsgAddressExt = Tlb.decodeFromBitString(BitString(1024).apply {
            writeBit(false)
            writeBit(true)
            writeInt(12, 9)
            writeBitString(BitString(12).also { bs ->
                repeat(12) {
                    bs.writeBit()
                }
            })
        })
        assertEquals(AddrExtern(12, BitString(12).also { bs ->
            repeat(12) {
                bs.writeBit()
            }
        }), addrExtern)
    }

    @Test
    fun testDecode() {
        // addr_extern$01 len:(## 9) external_address:(bits len) = MsgAddressExt;
        val decoder = TlbDecoder(Cell(BitString(1024) {
            writeBits(false, true)
            writeUInt(12u, 9)
            writeBitString(BitString(12).also { bs ->
                repeat(12) {
                    bs.writeBit()
                }
            })
        })).apply {
            typeCombinator("MsgAddressExt") {
                constructor("addr_extern", BitString(false, true)) {
                    val len = field("len", TlbDecoder.UintType(TlbDecoder.IntConstant(9)))
                    field("external_address", TlbDecoder.BitStringType(len))
                }
            }
        }

        val result = decoder.decode("MsgAddressExt")
        println("decoded: $result")
    }

    /*
    transaction$0111 account_addr:bits256 lt:uint64
  prev_trans_hash:bits256 prev_trans_lt:uint64 now:uint32
  outmsg_cnt:uint15
  orig_status:AccountStatus end_status:AccountStatus
  ^[ in_msg:(Maybe ^(Message Any)) out_msgs:(HashmapE 15 ^(Message Any)) ]
  total_fees:CurrencyCollection state_update:^(HASH_UPDATE Account)
  description:^TransactionDescr = Transaction;
     */
    @Test
    fun testParseData() {
        val dataBitString =
            BitString("B5EE9C72410207010001B60003B5783DFD552E63729B472FCBCC8C45EBCC6691702558B68EC7527E1BA403A0F31A80000178EBA3796472ADF624C1E07A11E874B48C0389A2C6BB3719FF2758796DF45E84D1D06E9A4DA00001788D30F8206621E332100014603282280102030101A004008272CCCDA69F223F0035678D71A9262F28CE9275C2F56D6DE7068299B4CC91758C6AAF146F6619F6BFB457283C19C0F9C7C686BF52FCABD212CB9888451865B3D2E002170C835C48C9BA3C186030D411050600FB48016173957EE1B849B00D183AE2954B185B888152D36268EE2C71389D441F42E5D50020F7F554B98DCA6D1CBF2F323117AF319A45C09562DA3B1D49F86E900E83CC6A0C9BA3C00614586000002F1D746F2C8CC43C664200000000343A3A38399D1797BA1736B297BA37B72FB3393AB4BA39AFB137BA103830BCB7BABA40009A27C827D800000000000000000300000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000005BC00000000000000000000000012D452DA449E50B8CF7DD27861F146122AFE1B546BB8B70FC8216F0C614139F8E04E095D058")
        println(dataBitString.toString(debug = true))


        val cell = Cell(
            "783DFD552E63729B472FCBCC8C45EBCC6691702558B68EC7527E1BA403A0F31A80000178EBA3796472ADF624C1E07A11E874B48C0389A2C6BB3719FF2758796DF45E84D1D06E9A4DA00001788D30F8206621E3321000146032822",
            Cell(
                "A_",
                Cell("48016173957EE1B849B00D183AE2954B185B888152D36268EE2C71389D441F42E5D50020F7F554B98DCA6D1CBF2F323117AF319A45C09562DA3B1D49F86E900E83CC6A100487AB000614586000002F24F6CDA38AC43DF8A000000000343A3A38399D1797BA1736B297BA37B72FB3393AB4BA39AFB137BA103830BCB7BABA4_")
            ),
            Cell("72B3894B2A29B4D7348ADE9D178977D08D938D715F93AE1A98723EBF60DF508F175F25E0EBB0DE12CD5B651989F2AA84E140072A7DFBEF010740E504DFA616A322"),
            Cell(
                "0C81828900487AB0186030D411_",
                Cell("27C928E000000000000000000300000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"),
                Cell("C00000000000000000000000012D452DA449E50B8CF7DD27861F146122AFE1B546BB8B70FC8216F0C614139F8E04")
            )
        )
        val decoder = TlbDecoder(cell).apply {
            // nothing$0 {X:Type} = Maybe X;
            // just$1 {X:Type} value:X = Maybe X;
            val maybe = typeCombinator("Maybe") {
                constructor("nothing", prefix = BitString(false))
                constructor("just", prefix = BitString(true)) {
                    field("value", argument<Any>(0))
                }
            }

            val hashmap = typeCombinator("Hashmap") {
                constructor("hm_edge") {
                }
            }

            val hashmapNode = typeCombinator("HashmapNode") {
                constructor("hmn_leaf") {
                    field("value", argument<Any>(1))
                }
                constructor("hmn_fork") {
//                    field("left", cellReference {
//
//                    })
                }
            }

            val hashmape = typeCombinator("HashmapE") {
                constructor("hme_empty", prefix = BitString(false))
                constructor("hme_root", prefix = BitString(true)) {
//                    field("root", cellReference {
//
//                    })
                }
            }
//            // left$0 {X:Type} {Y:Type} value:X = Either X Y;
//            // right$1 {X:Type} {Y:Type} value:Y = Either X Y;
//            val either = typeCombinator("Either") {
//                constructor("left", prefix = BitString(false)) {
//                    field("value", arguments[0])
//                }
//                constructor("right", prefix = BitString(true)) {
//                    field("value", arguments[1])
//                }
//            }
            // anycast_info$_ depth:(#<= 30) { depth >= 1 } rewrite_pfx:(bits depth) = Anycast;
            val anycast = typeCombinator("Anycast") {
                constructor("anycast_info") {
                    val depth = field("depth", TlbDecoder.UintType(30))
                    field("rewrite_pfx", TlbDecoder.BitStringType(depth))
                }
            }

            //addr_std$10 anycast:(Maybe Anycast) workchain_id:int8 address:bits256  = MsgAddressInt;
            //addr_var$11 anycast:(Maybe Anycast) addr_len:(## 9) workchain_id:int32 address:(bits addr_len) = MsgAddressInt;
            val msgAddressInit = typeCombinator("MsgAddressInt") {
                constructor("addr_std", prefix = BitString(true, false)) {
                    field("anycast", maybe(anycast))
                    field("workchain_id", int(8))
                    field("address", bitString(256))
                }
                constructor("addr_var", prefix = BitString(true, true)) {
                    field("anycast", maybe(anycast))
                    val addrLen = field("addr_len", uint(9))
                    field("workchain_id", int(32))
                    field("address", bitString(addrLen))
                }
            }


            val varUInteger = typeCombinator("VarUInteger") {
                constructor("var_uint") {
                    val len = field("len", uint(4)) // TODO: #< n
                    field("value", uint(len * 8))
                }
            }

            //extra_currencies$_ dict:(HashmapE 32 (VarUInteger 32)) = ExtraCurrencyCollection;
            val extraCurrencyCollection = typeCombinator("extra_currencies") {
                constructor("extra_currencies") {
                    field("dict", hashmape(TlbDecoder.IntConstant(32), varUInteger(TlbDecoder.IntConstant(32))))
                }
            }

            //nanograms$_ amount:(VarUInteger 16) = Grams;
            val grams = typeCombinator("Grams") {
                constructor("nanograms") {
                    field("amount", varUInteger(TlbDecoder.IntConstant(16)))
                }
            }

            //currencies$_ grams:Grams other:ExtraCurrencyCollection = CurrencyCollection;
            val currencyCollection = typeCombinator("CurrencyCollection") {
                constructor("currencies") {
                    field("grams", grams)
                    field("other", extraCurrencyCollection)
                }
            }

            //int_msg_info$0 ihr_disabled:Bool bounce:Bool bounced:Bool
            //  src:MsgAddressInt dest:MsgAddressInt
            //  value:CurrencyCollection ihr_fee:Grams fwd_fee:Grams
            //  created_lt:uint64 created_at:uint32 = CommonMsgInfo;
            //ext_in_msg_info$10 src:MsgAddressExt dest:MsgAddressInt
            //  import_fee:Grams = CommonMsgInfo;
            //ext_out_msg_info$11 src:MsgAddressInt dest:MsgAddressExt
            //  created_lt:uint64 created_at:uint32 = CommonMsgInfo;
            val commonMsgInfo = typeCombinator("CommonMsgInfo") {
                constructor("int_msg_info", prefix = BitString(false)) {
                    field("ihr_disabled", bool())
                    field("bounce", bool())
                    field("bounced", bool())
                    field("src", msgAddressInit)
                    field("dest", msgAddressInit)
                    field("value", currencyCollection)
                    field("ihr_fee", grams)
                    field("fwd_fee", grams)
                    field("created_lt", uint(64))
                    field("created_at", uint(32))
                }
                constructor("ext_in_msg_info", prefix = BitString(true, false))
            }

            // message$_ {X:Type} info:CommonMsgInfo
            //  init:(Maybe (Either StateInit ^StateInit))
            //  body:(Either X ^X) = Message X;
            val message = typeCombinator("Message") {
                constructor("message") {
                    field("info", commonMsgInfo)
                }
            }

            // acc_state_uninit$00 = AccountStatus;
            //            acc_state_frozen$01 = AccountStatus;
            //            acc_state_active$10 = AccountStatus;
            //            acc_state_nonexist$11 = AccountStatus;
            val accountStatus = typeCombinator("AccountStatus") {
                constructor("acc_state_uninit", prefix = BitString(false, false))
                constructor("acc_state_frozen", prefix = BitString(false, true))
                constructor("acc_state_active", prefix = BitString(true, false))
                constructor("acc_state_nonexist", prefix = BitString(true, true))
            }

            // transaction$0111 account_addr:bits256 lt:uint64
            //  prev_trans_hash:bits256 prev_trans_lt:uint64 now:uint32
            //  outmsg_cnt:uint15
            //  orig_status:AccountStatus end_status:AccountStatus
            //  ^[ in_msg:(Maybe ^(Message Any)) out_msgs:(HashmapE 15 ^(Message Any)) ]
            //  total_fees:CurrencyCollection state_update:^(HASH_UPDATE Account)
            //  description:^TransactionDescr = Transaction;
            val transaction = typeCombinator("Transaction") {
                constructor("transaction", prefix = BitString(false, true, true, true)) {
                    field("account_addr", bitString(256))
                    field("lt", ulong(64))
                    field("prev_trans_hash", bitString(256))
                    field("prev_trans_lt", ulong(64))
                    field("now", uint(32))
                    field("outmsg_cnt", uint(15))
                    field("orig_status", accountStatus)
                    field("end_status", accountStatus)
                    field("[]", cellReference {
                        constructor("[]") {
                            field("in_msg", maybe.invoke(cellReference {
                                message(any())
                            }))
                        }
                    })
                }
            }
        }
        val result = decoder.decode("Transaction")
        println(result)
    }
}