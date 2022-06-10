package org.ton.block

import org.ton.hashmap.AugDictionary

// _ (AugDictionary 256 OutMsg CurrencyCollection) = OutMsgDescr;
typealias OutMsgDescr = AugDictionary<OutMsg, CurrencyCollection>