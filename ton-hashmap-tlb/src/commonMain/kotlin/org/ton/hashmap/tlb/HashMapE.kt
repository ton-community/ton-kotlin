package org.ton.hashmap.tlb

import org.ton.hashmap.EmptyHashMapE
import org.ton.hashmap.HashMapE
import org.ton.hashmap.RootHashMapE
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.TlbDecoder
import org.ton.tlb.TlbEncoder

object HashMapETlbCombinator : TlbCombinator<HashMapE<Any>>(
    constructors = listOf(EmptyHashMapE.tlbCodec, RootHashMapE.tlbCodec)
) {
    override fun encode(
        cellWriter: CellWriter,
        value: HashMapE<Any>,
        typeParam: TlbEncoder<Any>?,
        param: Int,
        negativeParam: ((Int) -> Unit)?
    ) {
        when (value) {
            is RootHashMapE -> RootHashMapETlbConstructor.encode(cellWriter, value, typeParam, param, negativeParam)
            is EmptyHashMapE -> EmptyHashMapETlbConstructor.encode(cellWriter, value, typeParam, param, negativeParam)
        }
    }

    override fun decode(
        cellReader: CellReader,
        typeParam: TlbDecoder<Any>?,
        param: Int,
        negativeParam: ((Int) -> Unit)?
    ): HashMapE<Any> {
        return if (cellReader.readBit()) {
            RootHashMapETlbConstructor.decode(cellReader, typeParam, param, negativeParam)
        } else {
            EmptyHashMapETlbConstructor.decode(cellReader, typeParam, param, negativeParam)
        }
    }
}

val HashMapE.Companion.tlbCodec get() = HashMapETlbCombinator

object EmptyHashMapETlbConstructor : TlbConstructor<EmptyHashMapE<Any>>(
    schema = "hme_empty\$0 {n:#} {X:Type} = HashmapE n X;"
) {
    override fun encode(
        cellWriter: CellWriter,
        value: EmptyHashMapE<Any>,
        typeParam: TlbEncoder<Any>?,
        param: Int,
        negativeParam: ((Int) -> Unit)?
    ) = Unit

    override fun decode(
        cellReader: CellReader,
        typeParam: TlbDecoder<Any>?,
        param: Int,
        negativeParam: ((Int) -> Unit)?
    ): EmptyHashMapE<Any> = EmptyHashMapE()
}

val EmptyHashMapE.Companion.tlbCodec get() = EmptyHashMapETlbConstructor

object RootHashMapETlbConstructor : TlbConstructor<RootHashMapE<Any>>(
    schema = "hme_root\$1 {n:#} {X:Type} root:^(Hashmap n X) = HashmapE n X;"
) {
    override fun encode(
        cellWriter: CellWriter,
        value: RootHashMapE<Any>,
        typeParam: TlbEncoder<Any>?,
        param: Int,
        negativeParam: ((Int) -> Unit)?
    ) {
        cellWriter.writeCell {
            HashMapEdgeTlbConstructor.encode(this, value.root, typeParam, param, negativeParam)
        }
    }

    override fun decode(
        cellReader: CellReader,
        typeParam: TlbDecoder<Any>?,
        param: Int,
        negativeParam: ((Int) -> Unit)?
    ): RootHashMapE<Any> {
        val root = HashMapEdgeTlbConstructor.decode(cellReader.readCell(), typeParam, param, negativeParam)
        return RootHashMapE(root)
    }
}

val RootHashMapE.Companion.tlbCodec get() = RootHashMapETlbConstructor
