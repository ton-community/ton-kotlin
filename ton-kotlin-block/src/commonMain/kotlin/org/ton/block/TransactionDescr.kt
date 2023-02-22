@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.bitstring.BitString
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbLoader
import org.ton.tlb.TlbObject
import org.ton.tlb.providers.TlbCombinatorProvider

@JsonClassDiscriminator("@type")
@Serializable
public sealed interface TransactionDescr : TlbObject {
    public companion object : TlbCombinatorProvider<TransactionDescr> by TransactionDescrTlbCombinator
}

private object TransactionDescrTlbCombinator : TlbCombinator<TransactionDescr>(
    TransactionDescr::class,
    TransOrd::class to TransOrd.tlbConstructor(),
    TransStorage::class to TransStorage.tlbConstructor(),
    TransTickTock::class to TransTickTock.tlbConstructor(),
    TransMergeInstall::class to TransMergeInstall.tlbConstructor(),
    TransMergePrepare::class to TransMergePrepare.tlbConstructor(),
    TransSplitInstall::class to TransSplitInstall.tlbConstructor(),
    TransSplitPrepare::class to TransSplitPrepare.tlbConstructor(),
) {
    override fun findTlbLoaderOrNull(bitString: BitString): TlbLoader<out TransactionDescr>? {
        return if (bitString.size < 4) {
            null
        } else {
            if (bitString[0]) { // 1
                null
            } else { // 0
                if (bitString[1]) { // 01
                    if (bitString[2]) { // 011
                        if (bitString[3]) { // 0111
                            TransMergeInstall.tlbConstructor()
                        } else { // 0110
                            TransMergePrepare.tlbConstructor()
                        }
                    } else { // 010
                        if (bitString[3]) { // 0101
                            TransSplitInstall.tlbConstructor()
                        } else { // 0100
                            TransSplitPrepare.tlbConstructor()
                        }
                    }
                } else { // 00
                    if (bitString[2]) { // 001
                        TransTickTock.tlbConstructor()
                    } else { // 000
                        if (bitString[3]) { // 0001
                            TransStorage.tlbConstructor()
                        } else { // 0000
                            TransOrd.tlbConstructor()
                        }
                    }
                }
            }
        }
    }
}
