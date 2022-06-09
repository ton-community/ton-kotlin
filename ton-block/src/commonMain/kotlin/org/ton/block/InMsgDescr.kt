package org.ton.block

import org.ton.hashmap.HashmapAugE

interface InMsgDescr : HashmapAugE<InMsg, ImportFees>

data class InMsgDescrData(
    private val _value: HashmapAugE<InMsg, ImportFees>
) : InMsgDescr, HashmapAugE<InMsg, ImportFees> by _value