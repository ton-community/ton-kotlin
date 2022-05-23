package org.ton.hashmap.tlb

import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.hashmap.EmptyHashMapE
import org.ton.hashmap.HashMapE
import org.ton.hashmap.RootHashMapE
import org.ton.tlb.*
import org.ton.tlb.exception.UnknownTlbConstructorException

class HashMapETlbCombinator<T : Any>(
    typeCodec: TlbCodec<T>
) : TlbCombinator<HashMapE<T>>() {
    private val rootConstructor = RootHashMapETlbConstructor(typeCodec)
    private val emptyConstructor = EmptyHashMapETlbConstructor<T>()

    override val constructors = listOf(rootConstructor, emptyConstructor)

    override fun encode(
        cellBuilder: CellBuilder,
        value: HashMapE<T>,
        param: Int,
        negativeParam: (Int) -> Unit
    ) {
        when (value) {
            is RootHashMapE -> cellBuilder.storeTlb(value, rootConstructor, param)
            is EmptyHashMapE -> cellBuilder.storeTlb(value, emptyConstructor, param)
        }
    }

    override fun decode(
        cellSlice: CellSlice,
        param: Int,
        negativeParam: (Int) -> Unit
    ): HashMapE<T> {
        return when (val id = cellSlice.loadBitString(1)) {
            rootConstructor.id -> cellSlice.loadTlb(rootConstructor, param)
            emptyConstructor.id -> cellSlice.loadTlb(emptyConstructor, param)
            else -> throw UnknownTlbConstructorException(id)
        }
    }
}

class EmptyHashMapETlbConstructor<X : Any> : TlbConstructor<EmptyHashMapE<X>>(
    schema = "hme_empty\$0 {n:#} {X:Type} = HashmapE n X;"
) {
    override val id: BitString = BitString(false)

    override fun encode(
        cellBuilder: CellBuilder,
        value: EmptyHashMapE<X>,
        param: Int,
        negativeParam: (Int) -> Unit
    ) = Unit

    override fun decode(
        cellSlice: CellSlice,
        param: Int,
        negativeParam: (Int) -> Unit
    ): EmptyHashMapE<X> = EmptyHashMapE()
}

class RootHashMapETlbConstructor<X : Any>(
    typeCodec: TlbCodec<X>
) : TlbConstructor<RootHashMapE<X>>(
    schema = "hme_root\$1 {n:#} {X:Type} root:^(Hashmap n X) = HashmapE n X;"
) {
    private val hashmapConstructor = HashMapEdgeTlbConstructor(typeCodec)

    override val id: BitString = BitString(1)

    override fun encode(
        cellBuilder: CellBuilder,
        value: RootHashMapE<X>,
        param: Int,
        negativeParam: (Int) -> Unit
    ) {
        cellBuilder.storeRef {
            storeTlb(value.root, hashmapConstructor, param)
        }
    }

    override fun decode(
        cellSlice: CellSlice,
        param: Int,
        negativeParam: (Int) -> Unit
    ): RootHashMapE<X> {
        val root = cellSlice.loadRef {
            cellSlice.loadTlb(hashmapConstructor, param)
        }
        return RootHashMapE(root)
    }
}
