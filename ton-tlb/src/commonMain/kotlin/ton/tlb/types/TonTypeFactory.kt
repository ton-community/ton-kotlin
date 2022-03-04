@file:Suppress("FunctionName")

package ton.tlb.types

interface TonTypeFactory : InbuiltTypeFactory, HashmapTypeFactory {
    // addr_none$00 = MsgAddressExt;
    // addr_extern$01 len:(## 9) external_address:(bits len) = MsgAddressExt;
    fun MsgAddressExt() = TypeCombinator("MsgAddressExt") {
        constructor("addr_none$00")
        constructor("addr_extern$01") {
            val len = field("len", uint(9))
            field("external_address", bitString(len))
        }
    }

    // anycast_info$_ depth:(#<= 30) { depth >= 1 } rewrite_pfx:(bits depth) = Anycast;
    fun Anycast() = TypeCombinator("Anycast") {
        constructor("anycast_info") {
            val depth = field("depth", lessThanOrEqualsInt(30))
            field("rewrite_pfx", bits(depth))
        }
    }

    // addr_std$10 anycast:(Maybe Anycast) workchain_id:int8 address:bits256 = MsgAddressInt;
    // addr_var$11 anycast:(Maybe Anycast) addr_len:(## 9) workchain_id:int32 address:(bits addr_len) = MsgAddressInt;
    fun MsgAddressInt() = TypeCombinator("MsgAddressInt") {
        constructor("addr_std$10") {
            field("anycast", Maybe(Anycast()))
            field("workchain_id", int8())
            field("address", bits256())
        }
        constructor("addr_var$11") {
            field("anycast", Maybe(Anycast()))
            val addrLen = field("addr_len", uint(9))
            field("workchain_id", int32())
            field("address", bits(addrLen))
        }
    }

    // var_uint$_ {n:#} len:(#< n) value:(uint (len * 8)) = VarUInteger n;
    fun VarUInteger(value: TypeExpression<Int>) = TypeCombinator("VarUInteger") {
        constructor("var_uint") {
            val len = field("len", lessThanInt(value))
            field("value", uint(len * 8))
        }
    }

    // var_int$_ {n:#} len:(#< n) value:(int (len * 8)) = VarInteger n;
    fun VarInteger(value: TypeExpression<Int>) = TypeCombinator("VarInteger") {
        constructor("var_int") {
            val len = field("len", lessThanInt(value))
            field("value", int(len * 8))
        }
    }

    // nanograms$_ amount:(VarUInteger 16) = Grams;
    fun Grams() = TypeCombinator("Grams") {
        constructor("nanograms") {
            field("amount", VarUInteger(constant(16)))
        }
    }

    // extra_currencies$_ dict:(HashmapE 32 (VarUInteger 32)) = ExtraCurrencyCollection;
    fun ExtraCurrencyCollection() = TypeCombinator("ExtraCurrencyCollection") {
        constructor("extra_currencies") {
            field("dict", HashmapE(constant(32), VarUInteger(constant(32))))
        }
    }

    // currencies$_ grams:Grams other:ExtraCurrencyCollection = CurrencyCollection;
    fun CurrencyCollection() = TypeCombinator("CurrencyCollection") {
        constructor("currencies") {
            field("grams", Grams())
            field("other", ExtraCurrencyCollection())
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
    fun CommonMsgInfo() = TypeCombinator("CommonMsgInfo") {
        constructor("int_msg_info$0") {
            field("ihr_disabled", Bool())
            field("bounce", Bool())
            field("bounced", Bool())
            field("src", MsgAddressInt())
            field("dest", MsgAddressInt())
            field("value", CurrencyCollection())
            field("ihr_fee", Grams())
            field("fwd_fee", Grams())
            field("created_lt", uint64())
            field("created_at", uint32())
        }
        constructor("ext_in_msg_info$10") {
            field("src", MsgAddressExt())
            field("dest", MsgAddressInt())
            field("import_fee", Grams())
        }
        constructor("ext_out_msg_info$11") {
            field("src", MsgAddressInt())
            field("dest", MsgAddressExt())
            field("created_lt", uint64())
            field("created_at", uint32())
        }
    }

    // acc_state_uninit$00 = AccountStatus;
    // acc_state_frozen$01 = AccountStatus;
    // acc_state_active$10 = AccountStatus;
    // acc_state_nonexist$11 = AccountStatus;
    fun AccountStatus() = TypeCombinator("AccountStatus") {
        constructor("acc_state_uninit$00")
        constructor("acc_state_frozen$01")
        constructor("acc_state_active$10")
        constructor("acc_state_nonexist$11")
    }

    // tick_tock$_ tick:Bool tock:Bool = TickTock;
    fun TickTock() = TypeCombinator("TickTock") {
        constructor("tick_tock") {
            field("tick", Bool())
            field("tock", Bool())
        }
    }

    // simple_lib$_ public:Bool root:^Cell = SimpleLib;
    fun SimpleLib() = TypeCombinator("SimpleLib") {
        constructor("simple_lib") {
            field("public", Bool())
            field("root", cellReference { cell() })
        }
    }

    // _ split_depth:(Maybe (## 5)) special:(Maybe TickTock)
    //  code:(Maybe ^Cell) data:(Maybe ^Cell)
    //  library:(HashmapE 256 SimpleLib) = StateInit;
    fun StateInit() = TypeCombinator("StateInit") {
        constructor("_") {
            field("split_depth", Maybe(uint(5)))
            field("special", Maybe(TickTock()))
            field("code", Maybe(cellReference { cell() }))
            field("data", Maybe(cellReference { cell() }))
            field("library", HashmapE(constant(256), SimpleLib()))
        }
    }

    // message$_ {X:Type} info:CommonMsgInfo
    //  init:(Maybe (Either StateInit ^StateInit))
    //  body:(Either X ^X) = Message X;
    fun Message(x: TypeExpression<*>) = TypeCombinator("Message") {
        constructor("message") {
            field("info", CommonMsgInfo())
            field("init", Maybe(Either(StateInit(), cellReference { StateInit() })))
            field("body", Either(x, cellReference { x }))
        }
    }

    // transaction$0111 account_addr:bits256 lt:uint64
    //  prev_trans_hash:bits256 prev_trans_lt:uint64 now:uint32
    //  outmsg_cnt:uint15
    //  orig_status:AccountStatus end_status:AccountStatus
    //  ^[ in_msg:(Maybe ^(Message Any)) out_msgs:(HashmapE 15 ^(Message Any)) ]
    //  total_fees:CurrencyCollection state_update:^(HASH_UPDATE Account)
    //  description:^TransactionDescr = Transaction;
    fun Transaction() = TypeCombinator("Transaction") {
        constructor("transaction$0111") {
            field("account_addr", bits256())
            field("lt", uint64())
            field("prev_trans_hash", bits256())
            field("prev_trans_lt", uint64())
            field("now", uint32())
            field("outmsg_cnt", uint(15))
            field("orig_status", AccountStatus())
            field("end_status", AccountStatus())
            unnamedField(cellReference {
                anonymousConstructor {
                    field("in_msg", Maybe(cellReference { Message(any()) }))
                    field("out_msg", HashmapE(constant(15), cellReference { Message(any()) }))
                }
            })
            field("total_fees", CurrencyCollection())
            field("state_update", cellReference {
                HASH_UPDATE(Account())
            })
            field("description", cellReference {
                TransactionDescr()
            })
        }
    }

    // update_hashes#72 {X:Type} old_hash:bits256 new_hash:bits256 = HASH_UPDATE X;
    fun HASH_UPDATE(x: TypeExpression<*>) = TypeCombinator("HASH_UPDATE") {
        constructor("update_hashes#72") {
            field("old_hash", bits256())
            field("new_hash", bits256())
        }
    }

    // storage_used$_ cells:(VarUInteger 7) bits:(VarUInteger 7) public_cells:(VarUInteger 7) = StorageUsed;
    fun StorageUsed() = TypeCombinator("StorageUsed") {
        constructor("storage_used") {
            field("cells", VarUInteger(constant(7)))
            field("bits", VarUInteger(constant(7)))
            field("public_cells", VarUInteger(constant(7)))
        }
    }

    // storage_used_short$_ cells:(VarUInteger 7) bits:(VarUInteger 7) = StorageUsedShort;
    fun StorageUsedShort() = TypeCombinator("StorageUsedShort") {
        constructor("storage_used_short") {
            field("cells", VarUInteger(constant(7)))
            field("bits", VarUInteger(constant(7)))
        }
    }

    // storage_info$_ used:StorageUsed last_paid:uint32 due_payment:(Maybe Grams) = StorageInfo;
    fun StorageInfo() = TypeCombinator("StorageInfo") {
        constructor("storage_info") {
            field("used", StorageUsed())
            field("last_paid", uint32())
            field("due_payment", Maybe(Grams()))
        }
    }

    // account_storage$_ last_trans_lt:uint64 balance:CurrencyCollection state:AccountState = AccountStorage;
    fun AccountStorage() = TypeCombinator("AccountStorage") {
        constructor("account_storage") {
            field("last_trans_lt", uint64())
            field("balance", CurrencyCollection())
            field("state", AccountState())
        }
    }

    // account_none$0 = Account;
    // account$1 addr:MsgAddressInt storage_stat:StorageInfo storage:AccountStorage = Account;
    fun Account() = TypeCombinator("Account") {
        constructor("account_none$0")
        constructor("account$1") {
            field("addr", MsgAddressInt())
            field("storage_stat", StorageInfo())
            field("storage", AccountStorage())
        }
    }

    // account_uninit$00 = AccountState;
    // account_active$1 _:StateInit = AccountState;
    // account_frozen$01 state_hash:bits256 = AccountState;
    fun AccountState() = TypeCombinator("AccountState") {
        constructor("account_uninit$00")
        constructor("account_active$1") {
            field("_", StateInit())
        }
        constructor("account_frozen$01") {
            field("state_hash", bits256())
        }
    }

    // trans_ord$0000 credit_first:Bool
    //  storage_ph:(Maybe TrStoragePhase)
    //  credit_ph:(Maybe TrCreditPhase)
    //  compute_ph:TrComputePhase action:(Maybe ^TrActionPhase)
    //  aborted:Bool bounce:(Maybe TrBouncePhase)
    //  destroyed:Bool
    //  = TransactionDescr;
    // trans_storage$0001 storage_ph:TrStoragePhase
    //  = TransactionDescr;
    fun TransactionDescr() = TypeCombinator("TransactionDescr") {
        constructor("trans_ord$0000") {
            field("credit_first", Bool())
            field("storage_ph", Maybe(TrStoragePhase()))
            field("credit_ph", Maybe(TrCreditPhase()))
            field("compute_ph", TrComputePhase())
            field("action", Maybe(cellReference { TrActionPhase() }))
            field("aborted", Bool())
            field("bounce", Maybe(TrBouncePhase()))
            field("destroyed", Bool())
        }
        constructor("trans_storage$0001") {
            field("storage_ph", TrStoragePhase())
        }
    }

    // tr_phase_storage$_ storage_fees_collected:Grams
    //  storage_fees_due:(Maybe Grams)
    //  status_change:AccStatusChange
    //  = TrStoragePhase;
    fun TrStoragePhase() = TypeCombinator("TrStoragePhase") {
        constructor("tr_phase_storage") {
            field("storage_fees_collected", Grams())
            field("storage_fees_due", Maybe(Grams()))
            field("status_change", AccStatusChange())
        }
    }

    // acst_unchanged$0 = AccStatusChange;  // x -> x
    // acst_frozen$10 = AccStatusChange;    // init -> frozen
    // acst_deleted$11 = AccStatusChange;   // frozen -> deleted
    fun AccStatusChange() = TypeCombinator("AccStatusChange") {
        constructor("acst_unchanged$0")
        constructor("acst_frozen$10")
        constructor("acst_deleted$11")
    }

    // tr_phase_credit$_ due_fees_collected:(Maybe Grams) credit:CurrencyCollection = TrCreditPhase;
    fun TrCreditPhase() = TypeCombinator("TrCreditPhase") {
        constructor("tr_phase_credit") {
            field("due_fees_collected", Maybe(Grams()))
            field("credit", CurrencyCollection())
        }
    }

    //tr_phase_compute_skipped$0 reason:ComputeSkipReason
    //  = TrComputePhase;
    //tr_phase_compute_vm$1 success:Bool msg_state_used:Bool
    //  account_activated:Bool gas_fees:Grams
    //  ^[ gas_used:(VarUInteger 7)
    //  gas_limit:(VarUInteger 7) gas_credit:(Maybe (VarUInteger 3))
    //  mode:int8 exit_code:int32 exit_arg:(Maybe int32)
    //  vm_steps:uint32
    //  vm_init_state_hash:bits256 vm_final_state_hash:bits256 ]
    //  = TrComputePhase;
    fun TrComputePhase() = TypeCombinator("TrComputePhase") {
        constructor("tr_phase_compute_skipped$0") {
            field("reason", ComputeSkipReason())
        }
        constructor("tr_phase_compute_vm$1") {
            field("success", Bool())
            field("msg_state_used", Bool())
            field("account_activated", Bool())
            field("gas_fees", Grams())
            unnamedField(cellReference {
                anonymousConstructor {
                    field("gas_used", VarUInteger(constant(7)))
                    field("gas_limit", VarUInteger(constant(7)))
                    field("gas_credit", Maybe(VarUInteger(constant(3))))
                    field("mode", int8())
                    field("exit_code", int32())
                    field("exit_arg", Maybe(int32()))
                    field("vm_steps", uint32())
                    field("vm_init_state_hash", bits256())
                    field("vm_final_state_hash", bits256())
                }
            })
        }
    }

    // cskip_no_state$00 = ComputeSkipReason;
    // cskip_bad_state$01 = ComputeSkipReason;
    // cskip_no_gas$10 = ComputeSkipReason;
    fun ComputeSkipReason() = TypeCombinator("ComputeSkipReason") {
        constructor("cskip_no_state$00")
        constructor("cskip_bad_state$01")
        constructor("cskip_no_gas$10")
    }

    // tr_phase_action$_ success:Bool valid:Bool no_funds:Bool
    //  status_change:AccStatusChange
    //  total_fwd_fees:(Maybe Grams) total_action_fees:(Maybe Grams)
    //  result_code:int32 result_arg:(Maybe int32) tot_actions:uint16
    //  spec_actions:uint16 skipped_actions:uint16 msgs_created:uint16
    //  action_list_hash:bits256 tot_msg_size:StorageUsedShort
    //  = TrActionPhase;
    fun TrActionPhase() = TypeCombinator("TrActionPhase") {
        constructor("tr_phase_action") {
            field("success", Bool())
            field("valid", Bool())
            field("no_funds", Bool())
            field("status_change", AccStatusChange())
            field("total_fwd_fees", Maybe(Grams()))
            field("total_action_fees", Maybe(Grams()))
            field("result_code", int32())
            field("result_arg", Maybe(int32()))
            field("tot_actions", uint16())
            field("spec_actions", uint16())
            field("skipped_actions", uint16())
            field("msgs_created", uint16())
            field("action_list_hash", bits256())
            field("tot_msg_size", StorageUsedShort())
        }
    }

    // tr_phase_bounce_negfunds$00 = TrBouncePhase;
    // tr_phase_bounce_nofunds$01 msg_size:StorageUsedShort
    //   req_fwd_fees:Grams = TrBouncePhase;
    // tr_phase_bounce_ok$1 msg_size:StorageUsedShort
    //   msg_fees:Grams fwd_fees:Grams = TrBouncePhase;
    fun TrBouncePhase() = TypeCombinator("TrBouncePhase") {
        constructor("tr_phase_bounce_negfunds$00")
        constructor("tr_phase_bounce_nofunds$01") {
            field("msg_size", StorageUsedShort())
            field("req_fwd_fees", Grams())
        }
        constructor("tr_phase_bounce_ok$1") {
            field("msg_size", StorageUsedShort())
            field("msg_fees", Grams())
            field("fwd_fees", Grams())
        }
    }
}