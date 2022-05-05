package org.ton.hashmap.tlb

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.hashmap.HashMapNode
import org.ton.hashmap.HashMapNodeFork
import org.ton.hashmap.HashMapNodeLeaf
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.TlbDecoder
import org.ton.tlb.TlbEncoder

object HashMapNodeTlbCombinator : TlbCombinator<HashMapNode<Any>>(
        constructors = listOf(HashMapNodeLeaf.tlbCodec, HashMapNodeFork.tlbCodec)
) {
    override fun encode(
            cellBuilder: CellBuilder,
            value: HashMapNode<Any>,
            typeParam: TlbEncoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
    ) {
        when (value) {
            is HashMapNodeLeaf -> HashMapNodeLeafTlbConstructor.encode(
                    cellBuilder,
                    value,
                    typeParam,
                    param,
                    negativeParam
            )
            is HashMapNodeFork -> HashMapNodeForkTlbCodec.encode(cellBuilder, value, typeParam, param, negativeParam)
        }
    }

    override fun decode(
            cellSlice: CellSlice,
            typeParam: TlbDecoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
    ): HashMapNode<Any> {
        return if (param == 0) {
            HashMapNodeLeafTlbConstructor.decode(cellSlice, typeParam, param, negativeParam)
        } else {
            HashMapNodeForkTlbCodec.decode(cellSlice, typeParam, param, negativeParam)
        }
    }
}

val HashMapNode.Companion.tlbCodec get() = HashMapNodeTlbCombinator

object HashMapNodeLeafTlbConstructor : TlbConstructor<HashMapNodeLeaf<Any>>(
        schema = "hmn_leaf#_ {X:Type} value:X = HashmapNode 0 X;"
) {
    override fun encode(
            cellBuilder: CellBuilder,
            value: HashMapNodeLeaf<Any>,
            typeParam: TlbEncoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
    ) {
        typeParam?.encode(cellBuilder, value.value, typeParam, param, negativeParam)
    }

    override fun decode(
            cellSlice: CellSlice,
            typeParam: TlbDecoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
    ): HashMapNodeLeaf<Any> {
        val value = typeParam?.decode(cellSlice, typeParam, param, negativeParam)
        return HashMapNodeLeaf(requireNotNull(value))
    }
}

val HashMapNodeLeaf.Companion.tlbCodec get() = HashMapNodeLeafTlbConstructor

object HashMapNodeForkTlbCodec : TlbConstructor<HashMapNodeFork<Any>>(
        schema = "hmn_fork#_ {n:#} {X:Type} left:^(Hashmap n X) right:^(Hashmap n X) = HashmapNode (n + 1) X;"
) {
    override fun encode(
            cellBuilder: CellBuilder,
            value: HashMapNodeFork<Any>,
            typeParam: TlbEncoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
    ) {
        val n = param - 1
        cellBuilder.storeRef {
            HashMapEdgeTlbConstructor.encode(this, value.left, typeParam, n, negativeParam)
        }
        cellBuilder.storeRef {
            HashMapEdgeTlbConstructor.encode(this, value.right, typeParam, n, negativeParam)
        }
    }

    override fun decode(
            cellSlice: CellSlice,
            typeParam: TlbDecoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
    ): HashMapNodeFork<Any> {
        val n = param - 1
        val left = HashMapEdgeTlbConstructor.decode(cellSlice.loadRef().beginParse(), typeParam, n, negativeParam)
        val right = HashMapEdgeTlbConstructor.decode(cellSlice.loadRef().beginParse(), typeParam, n, negativeParam)
        return HashMapNodeFork(left, right)
    }
}

val HashMapNodeFork.Companion.tlbCodec get() = HashMapNodeForkTlbCodec