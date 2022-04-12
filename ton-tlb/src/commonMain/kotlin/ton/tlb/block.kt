package ton.tlb

import ton.cell.CellReader

fun CellReader.MsgAddressExt(): TypeExpression {
    check(!readBit())
    return if (readBit()) {
        addr_extern()
    } else {
        addr_none()
    }
}

// addr_none$00 = MsgAddressExt;
private fun CellReader.addr_none() = type("addr_none")

// addr_extern$01 len:(## 9) external_address:(bits len) = MsgAddressExt;
private fun CellReader.addr_extern() = type("addr_extern") {
    set("len") { uint(9) }
    set("external_address") { bits(get("len")) }
}

// anycast_info$_ depth:(#<= 30) { depth >= 1 } rewrite_pfx:(bits depth) = Anycast;
fun CellReader.Anycast() = type("anycast_info") {
    set("depth") { leq(30) }
    set("rewrite_pfx") { bits(get("depth")) }
}

fun CellReader.MsgAddressInt(): TypeExpression {
    check(readBit())
    return if (readBit()) {
        addr_var()
    } else {
        addr_std()
    }
}

// addr_var$11 anycast:(Maybe Anycast) addr_len:(## 9) workchain_id:int32 address:(bits addr_len) = MsgAddressInt;
private fun CellReader.addr_var() = type("addr_var") {
    set("anycast") { Maybe { Anycast() } }
    set("addr_len") { uint(9) }
    set("workchain_id", ::int32)
    set("address") { bits(get("addr_len")) }
}

// addr_std$10 anycast:(Maybe Anycast) workchain_id:int8 address:bits256  = MsgAddressInt;
private fun CellReader.addr_std() = type("addr_std") {
    set("anycast") { Maybe { Anycast() } }
    set("workchain_id", ::int8)
    set("address", ::bits256)
}

fun CellReader.MsgAddress(): TypeExpression {
    val prefix = readBit()
    readPosition--
    return if (prefix) {
        MsgAddressInt()
    } else {
        MsgAddressExt()
    }
}

// var_int$_ {n:#} len:(#< n) value:(int (len * 8)) = VarInteger n;
fun CellReader.VarInteger(n: TypeExpression) = type("var_int") {
    set("len") { les(n) }
    set("value") { int(get("len").toInt() * 8) }
}

// var_uint$_ {n:#} len:(#< n) value:(uint (len * 8)) = VarUInteger n;
fun CellReader.VarUInteger(n: TypeExpression) = type("var_uint") {
    set("len") { les(n) }
    set("value") { uint(get("len").toInt() * 8) }
}

// nanograms$_ amount:(VarUInteger 16) = Grams;
fun CellReader.Grams() = type("nanograms") {
    set("amount") { VarUInteger(value(16)) }
}

// extra_currencies$_ dict:(HashmapE 32 (VarUInteger 32)) = ExtraCurrencyCollection;
fun CellReader.ExtraCurrencyCollection() = type("extra_currencies") {
    set("dict") { HashmapE(value(32)) { VarUInteger(value(32)) } }
}

// currencies$_ grams:Grams other:ExtraCurrencyCollection = CurrencyCollection;
fun CellReader.CurrencyCollection() = type("currencies") {
    set("grams", ::Grams)
    set("other", ::ExtraCurrencyCollection)
}

fun CellReader.CommonMsgInfo(): TypeExpression {
    return if (readBit()) {
        if (readBit()) {
            ext_out_msg_info()
        } else {
            ext_in_msg_info()
        }
    } else {
        int_msg_info()
    }
}

// ext_out_msg_info$11 src:MsgAddressInt dest:MsgAddressExt created_lt:uint64 created_at:uint32 = CommonMsgInfo;
private fun CellReader.ext_out_msg_info() = type("ext_out_msg_info") {
    set("src", ::MsgAddressInt)
    set("dest", ::MsgAddressExt)
    set("created_lt", ::uint64)
    set("created_at", ::uint32)
}

// ext_in_msg_info$10 src:MsgAddressExt dest:MsgAddressInt import_fee:Grams = CommonMsgInfo;
private fun CellReader.ext_in_msg_info() = type("ext_in_msg_info") {
    set("src", ::MsgAddressExt)
    set("dest", ::MsgAddressInt)
    set("import_fee", ::Grams)
}

// int_msg_info$0 ihr_disabled:Bool bounce:Bool bounced:Bool
//  src:MsgAddressInt dest:MsgAddressInt
//  value:CurrencyCollection ihr_fee:Grams fwd_fee:Grams
//  created_lt:uint64 created_at:uint32 = CommonMsgInfo;
private fun CellReader.int_msg_info() = type("int_msg_info") {
    set("ihr_disabled", ::Bool)
    set("bounce", ::Bool)
    set("bounced", ::Bool)
    set("src", ::MsgAddressInt)
    set("dest", ::MsgAddressInt)
    set("value", ::CurrencyCollection)
    set("ihr_fee", ::Grams)
    set("fwd_fee", ::Grams)
    set("created_lt", ::uint64)
    set("created_at", ::uint32)
}

// tick_tock$_ tick:Bool tock:Bool = TickTock;
fun CellReader.TickTock() = type("tick_tock") {
    set("tick", ::Bool)
    set("tock", ::Bool)
}

// simple_lib$_ public:Bool root:^Cell = SimpleLib;
fun CellReader.SimpleLib() = type("simple_lib") {
    set("public", ::Bool)
    set("root") { cellReference { cell() } }
}

// _ split_depth:(Maybe (## 5))
//  special:(Maybe TickTock)
//  code:(Maybe ^Cell)
//  data:(Maybe ^Cell)
//  library:(HashmapE 256 SimpleLib) = StateInit;
fun CellReader.StateInit() = type("_") {
    set("split_depth") { Maybe { uint(5) } }
    set("special") { Maybe(::TickTock) }
    set("code") { Maybe { cellReference { cell() } } }
    set("data") { Maybe { cellReference { cell() } } }
    set("library") { HashmapE(value(256), ::SimpleLib) }
}

// message$_ {X:Type} info:CommonMsgInfo
//  init:(Maybe (Either StateInit ^StateInit))
//  body:(Either X ^X) = Message X;
fun CellReader.Message(x: () -> TypeExpression) = type("message") {
    set("info", ::CommonMsgInfo)
    set("init") { Maybe { Either(::StateInit) { cellReference { StateInit() } } } }
    set("body") { Either(x) { cellReference { x() } } }
}

// acc_state_uninit$00 = AccountStatus;
// acc_state_frozen$01 = AccountStatus;
// acc_state_active$10 = AccountStatus;
// acc_state_nonexist$11 = AccountStatus;
fun CellReader.AccountStatus(): TypeExpression = if (readBit()) {
    if (readBit()) acc_state_nonexist()
    else acc_state_active()
} else {
    if (readBit()) acc_state_frozen()
    else acc_state_uninit()
}

private fun CellReader.acc_state_nonexist() = type("acc_state_nonexist")
private fun CellReader.acc_state_active() = type("acc_state_active")
private fun CellReader.acc_state_frozen() = type("acc_state_frozen")
private fun CellReader.acc_state_uninit() = type("acc_state_uninit")

fun CellReader.AccountState(): TypeExpression = if (readBit()) {
    account_active()
} else if (readBit()) {
    account_frozen()
} else {
    account_uninit()
}

// account_active$1 _:StateInit = AccountState;
private fun CellReader.account_active() = type("account_active") {
    set("_", ::StateInit)
}

// account_frozen$01 state_hash:bits256 = AccountState;
private fun CellReader.account_frozen() = type("account_frozen") {
    set("state_hash", ::bits256)
}

private fun CellReader.account_uninit() = type("account_uninit")

// storage_used$_ cells:(VarUInteger 7) bits:(VarUInteger 7) public_cells:(VarUInteger 7) = StorageUsed;
fun CellReader.StorageUsed() = type("storage_used") {
    set("cells") { VarUInteger(value(7)) }
    set("bits") { VarUInteger(value(7)) }
    set("public_cells") { VarUInteger(value(7)) }
}

// storage_used_short$_ cells:(VarUInteger 7) bits:(VarUInteger 7) = StorageUsedShort;
fun CellReader.StorageUsedShort() = type("storage_used_short") {
    set("cells") { VarUInteger(value(7)) }
    set("bits") { VarUInteger(value(7)) }
}

// storage_info$_ used:StorageUsed last_paid:uint32 due_payment:(Maybe Grams) = StorageInfo;
fun CellReader.StorageInfo() = type("storage_info") {
    set("used", ::StorageUsed)
    set("last_paid", ::uint32)
    set("due_payment") { Maybe(::Grams) }
}

fun CellReader.AccountStorage() = type("account_storage") {
    set("last_trans_lt", ::uint64)
    set("balance", ::CurrencyCollection)
    set("state", ::AccountState)
}

fun CellReader.Account(): TypeExpression = if (readBit()) account() else account_none()

// account$1 addr:MsgAddressInt storage_stat:StorageInfo  storage:AccountStorage = Account;
private fun CellReader.account() = type("account") {
    set("addr", ::MsgAddressInt)
    set("storage_stat", ::StorageInfo)
    set("storage", ::AccountStorage)
}

// account_none$0 = Account;
private fun CellReader.account_none() = type("account_none")

// update_hashes#72 {X:Type} old_hash:bits256 new_hash:bits256 = HASH_UPDATE X;
fun CellReader.HASH_UPDATE(x: () -> TypeExpression): TypeExpression {
    check(readInt(8) == 0x72)
    return type("update_hashes") {
        set("old_hash", ::bits256)
        set("new_hash", ::bits256)
    }
}

// split_merge_info$_ cur_shard_pfx_len:(## 6) acc_split_depth:(## 6) this_addr:bits256 sibling_addr:bits256
//  = SplitMergeInfo;
fun CellReader.SplitMergeInfo() = type("split_merge_info") {
    set("cur_shard_pfx_len") { uint(6) }
    set("acc_split_depth") { uint(6) }
    set("this_addr", ::bits256)
    set("sibling_addr", ::bits256)
}

// acst_unchanged$0 = AccStatusChange;  // x -> x
// acst_frozen$10 = AccStatusChange;    // init -> frozen
// acst_deleted$11 = AccStatusChange;   // frozen -> deleted
fun CellReader.AccStatusChange() = if (readBit()) if (readBit()) acst_deleted() else acst_frozen() else acst_unchanged()
private fun CellReader.acst_deleted() = type("acst_deleted")
private fun CellReader.acst_frozen() = type("acst_frozen")
private fun CellReader.acst_unchanged() = type("acst_unchanged")

// tr_phase_storage$_ storage_fees_collected:Grams
//  storage_fees_due:(Maybe Grams)
//  status_change:AccStatusChange
//  = TrStoragePhase;
fun CellReader.TrStoragePhase() = type("tr_phase_storage") {
    set("storage_fees_collected", ::Grams)
    set("storage_fees_due") { Maybe(::Grams) }
    set("status_change", ::AccStatusChange)
}

// tr_phase_credit$_ due_fees_collected:(Maybe Grams) credit:CurrencyCollection = TrCreditPhase;
fun CellReader.TrCreditPhase() = type("tr_phase_credit") {
    set("due_fees_collected") { Maybe(::Grams) }
    set("credit", ::CurrencyCollection)
}

fun CellReader.TrComputePhase() = if (readBit()) tr_phase_compute_vm() else tr_phase_compute_skipped()

// tr_phase_compute_vm$1 success:Bool msg_state_used:Bool
//  account_activated:Bool gas_fees:Grams
//  ^[ gas_used:(VarUInteger 7)
//  gas_limit:(VarUInteger 7) gas_credit:(Maybe (VarUInteger 3))
//  mode:int8 exit_code:int32 exit_arg:(Maybe int32)
//  vm_steps:uint32
//  vm_init_state_hash:bits256 vm_final_state_hash:bits256 ]
//  = TrComputePhase;
private fun CellReader.tr_phase_compute_vm() = type("tr_phase_compute_vm") {
    set("success", ::Bool)
    set("msg_state_used", ::Bool)
    set("account_activated", ::Bool)
    set("gas_fees", ::Grams)
    set("_") {
        cellReference {
            type("_") {
                set("gas_used") { VarUInteger(value(7)) }
                set("gas_limit") { VarUInteger(value(7)) }
                set("gas_credit") { Maybe { VarUInteger(value(3)) } }
                set("mode", ::int8)
                set("exit_code", ::int32)
                set("exit_arg") { Maybe(::int32) }
                set("vm_steps", ::uint32)
                set("vm_init_state_hash", ::bits256)
                set("vm_final_state_hash", ::bits256)
            }
        }
    }
}

// tr_phase_compute_skipped$0 reason:ComputeSkipReason = TrComputePhase;
private fun CellReader.tr_phase_compute_skipped() = type("tr_phase_compute_skipped") {
    set("reason", ::ComputeSkipReason)
}

// cskip_no_state$00 = ComputeSkipReason;
// cskip_bad_state$01 = ComputeSkipReason;
// cskip_no_gas$10 = ComputeSkipReason;
fun CellReader.ComputeSkipReason(): TypeExpression {
    return if (readBit()) {
        check(!readBit())
        cskip_no_gas()
    } else {
        if (readBit()) cskip_bad_state()
        else cskip_no_state()
    }
}

private fun CellReader.cskip_no_gas() = type("cskip_no_gas")
private fun CellReader.cskip_bad_state() = type("cskip_bad_state")
private fun CellReader.cskip_no_state() = type("cskip_no_state")

// tr_phase_action$_ success:Bool valid:Bool no_funds:Bool
//  status_change:AccStatusChange
//  total_fwd_fees:(Maybe Grams) total_action_fees:(Maybe Grams)
//  result_code:int32 result_arg:(Maybe int32) tot_actions:uint16
//  spec_actions:uint16 skipped_actions:uint16 msgs_created:uint16
//  action_list_hash:bits256 tot_msg_size:StorageUsedShort
//  = TrActionPhase;
fun CellReader.TrActionPhase() = type("tr_phase_action") {
    set("success", ::Bool)
    set("valid", ::Bool)
    set("no_funds", ::Bool)
    set("status_change", ::AccStatusChange)
    set("total_fwd_fees") { Maybe(::Grams) }
    set("total_action_fees") { Maybe(::Grams) }
    set("result_code", ::int32)
    set("result_arg") { Maybe(::int32) }
    set("tot_actions", ::uint16)
    set("spec_actions", ::uint16)
    set("skipped_actions", ::uint16)
    set("msgs_created", ::uint16)
    set("action_list_hash", ::bits256)
    set("tot_msg_size", ::StorageUsedShort)
}

fun CellReader.TrBouncePhase() = if (readBit()) {
    tr_phase_bounce_ok()
} else {
    if (readBit()) tr_phase_bounce_nofunds()
    else tr_phase_bounce_negfunds()
}

// tr_phase_bounce_ok$1 msg_size:StorageUsedShort msg_fees:Grams fwd_fees:Grams = TrBouncePhase;
private fun CellReader.tr_phase_bounce_ok() = type("tr_phase_bounce_ok") {
    set("msg_size", ::StorageUsedShort)
    set("msg_fees", ::Grams)
    set("fwd_fees", ::Grams)
}

// tr_phase_bounce_nofunds$01 msg_size:StorageUsedShort req_fwd_fees:Grams = TrBouncePhase;
private fun CellReader.tr_phase_bounce_nofunds() = type("tr_phase_bounce_nofunds") {
    set("msg_size", ::StorageUsedShort)
    set("req_fwd_fees", ::Grams)
}

private fun CellReader.tr_phase_bounce_negfunds() = type("tr_phase_bounce_negfunds")

fun CellReader.TransactionDescr(): TypeExpression {
    check(!readBit())
    return if (readBit()) if (readBit()) if (readBit()) trans_merge_install() else trans_merge_prepare()
    else if (readBit()) trans_split_install() else trans_split_prepare()
    else if (readBit()) trans_tick_tock() else if (readBit()) trans_storage() else trans_ord()
}

// trans_merge_install$0111 split_info:SplitMergeInfo
//  prepare_transaction:^Transaction
//  storage_ph:(Maybe TrStoragePhase)
//  credit_ph:(Maybe TrCreditPhase)
//  compute_ph:TrComputePhase action:(Maybe ^TrActionPhase)
//  aborted:Bool destroyed:Bool
//  = TransactionDescr;
private fun CellReader.trans_merge_install() = type("trans_merge_install") {
    set("split_info", ::SplitMergeInfo)
    set("prepare_transaction") { cellReference { Transaction() } }
    set("storage_ph") { Maybe(::TrStoragePhase) }
    set("credit_ph") { Maybe(::TrCreditPhase) }
    set("compute_ph", ::TrComputePhase)
    set("action") { Maybe { cellReference { TrActionPhase() } } }
    set("aborted", ::Bool)
    set("destroyed", ::Bool)
}

// trans_merge_prepare$0110 split_info:SplitMergeInfo
//  storage_ph:TrStoragePhase aborted:Bool
//  = TransactionDescr;
private fun CellReader.trans_merge_prepare() = type("trans_merge_prepare") {
    set("split_info", ::SplitMergeInfo)
    set("storage_ph", ::TrStoragePhase)
    set("aborted", ::Bool)
}

// trans_split_install$0101 split_info:SplitMergeInfo
//  prepare_transaction:^Transaction
//  installed:Bool = TransactionDescr;
private fun CellReader.trans_split_install() = type("trans_split_install") {
    set("split_info", ::SplitMergeInfo)
    set("prepare_transaction") { cellReference { Transaction() } }
    set("installed", ::Bool)
}

// trans_split_prepare$0100 split_info:SplitMergeInfo
//  storage_ph:(Maybe TrStoragePhase)
//  compute_ph:TrComputePhase action:(Maybe ^TrActionPhase)
//  aborted:Bool destroyed:Bool
//  = TransactionDescr;
private fun CellReader.trans_split_prepare() = type("trans_split_prepare") {
    set("split_info", ::SplitMergeInfo)
    set("storage_ph") { Maybe(::TrStoragePhase) }
    set("compute_ph", ::TrComputePhase)
    set("action") { Maybe { cellReference { TrActionPhase() } } }
    set("aborted", ::Bool)
    set("destroyed", ::Bool)
}

// trans_tick_tock$001 is_tock:Bool storage_ph:TrStoragePhase
//  compute_ph:TrComputePhase action:(Maybe ^TrActionPhase)
//  aborted:Bool destroyed:Bool = TransactionDescr;
private fun CellReader.trans_tick_tock() = type("trans_tick_tock") {
    set("is_tock", ::Bool)
    set("storage_ph", ::TrStoragePhase)
    set("compute_ph", ::TrComputePhase)
    set("action") { Maybe { cellReference { TrActionPhase() } } }
    set("aborted", ::Bool)
    set("destroyed", ::Bool)
}

private fun CellReader.trans_storage() = type("trans_storage") {
    set("storage_ph", ::TrStoragePhase)
}

// trans_ord$0000 credit_first:Bool
//  storage_ph:(Maybe TrStoragePhase)
//  credit_ph:(Maybe TrCreditPhase)
//  compute_ph:TrComputePhase action:(Maybe ^TrActionPhase)
//  aborted:Bool bounce:(Maybe TrBouncePhase)
//  destroyed:Bool
//  = TransactionDescr;
private fun CellReader.trans_ord() = type("trans_ord") {
    set("credit_first", ::Bool)
    set("storage_ph") { Maybe(::TrStoragePhase) }
    set("credit_ph") { Maybe(::TrCreditPhase) }
    set("compute_ph", ::TrComputePhase)
    set("action") { Maybe { cellReference { TrActionPhase() } } }
    set("aborted", ::Bool)
    set("bounce") { Maybe { TrBouncePhase() } }
    set("destroyed", ::Bool)
}

// transaction$0111 account_addr:bits256 lt:uint64
//  prev_trans_hash:bits256 prev_trans_lt:uint64 now:uint32
//  outmsg_cnt:uint15
//  orig_status:AccountStatus end_status:AccountStatus
//  ^[ in_msg:(Maybe ^(Message Any)) out_msgs:(HashmapE 15 ^(Message Any)) ]
//  total_fees:CurrencyCollection state_update:^(HASH_UPDATE Account)
//  description:^TransactionDescr = Transaction;
fun CellReader.Transaction(): TypeExpression {
    check(readInt(4) == 0b0111)
    return type("transaction") {
        set("account_addr", ::bits256)
        set("lt", ::uint64)
        set("prev_trans_hash", ::bits256)
        set("prev_trans_lt", ::uint64)
        set("now", ::uint32)
        set("outmsg_cnt") { uint(15) }
        set("orig_status", ::AccountStatus)
        set("end_status", ::AccountStatus)
        set("_") {
            cellReference {
                type("_") {
                    set("in_msg") { Maybe { cellReference { Message(::Any) } } }
                    set("out_msg") { HashmapE(value(15)) { cellReference { Message(::Any) } } }
                }
            }
        }
        set("total_fees", ::CurrencyCollection)
        set("state_update") { cellReference { HASH_UPDATE(::Account) } }
        set("description") { cellReference { TransactionDescr() } }
    }
}

