import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.hashmap.*

fun main() {
    val edge = edge(
        label = label(),
        node = fork(
            left = edge(label("111"), leaf("foo")), // 0
            right = edge(
                fork( // 1
                    left = edge(leaf("bar")), // 10
                    right = edge(label("01111"), leaf("buz")) // 1101
                )
            )
        )
    )
    println(edge)
    NodeIterator(edge).forEach {
        println(it.first.toBinary() + " " + it.second)
    }
}

fun label(string: String = "") = HmLabel(BitString.binary(string))
fun <T> edge(label: HmLabel, node: HashMapNode<T>) = HmEdge(label, node)
fun <T> edge(node: HashMapNode<T>) = HmEdge(label(), node)
fun <T> leaf(value: T) = HmnLeaf(value)
fun <T> fork(left: HmEdge<T>, right: HmEdge<T>) = HmnFork(left, right)
fun <T> fork(left: HashMapNode<T>, right: HashMapNode<T>) = HmnFork(edge(left), edge(right))


class NodeIterator<T>(
    start: HmEdge<T>
) : AbstractIterator<Pair<BitString, T>>() {
    val state = ArrayDeque<WalkState<T>>()

    init {
        addState(start.label.toBitString(), start.node)
    }

    private fun addState(prefix: BitString, node: HashMapNode<T>) {
        when (node) {
            is HmnFork<T> -> state.addFirst(WalkState.Fork(prefix, node))
            is HmnLeaf<T> -> state.addFirst(WalkState.Leaf(prefix, node))
            else -> done()
        }
    }

    sealed class WalkState<T>(open val node: HashMapNode<T>) {
        abstract fun step(): Pair<BitString, HashMapNode<T>>?

        class Leaf<T>(
            private val prefix: BitString,
            override val node: HmnLeaf<T>
        ) : WalkState<T>(node) {
            var visited = false

            override fun step(): Pair<BitString, HashMapNode<T>>? {
                if (visited) return null
                visited = true
                return prefix to node
            }
        }

        class Fork<T>(
            val prefix: BitString,
            override val node: HmnFork<T>
        ) : WalkState<T>(node) {
            private var leftVisited = false
            private var rightVisited = false

            override fun step(): Pair<BitString, HashMapNode<T>>? {
                return if (leftVisited) {
                    if (rightVisited) {
                        null
                    } else {
                        rightVisited = true
                        val newPrefix = CellBuilder().apply {
                            storeBits(prefix)
                            storeBit(true)
                            storeBits(node.right.value.label.toBitString())
                        }.bits
                        newPrefix to node.right.value.node
                    }
                } else {
                    leftVisited = true
                    val newPrefix = CellBuilder().apply {
                        storeBits(prefix)
                        storeBit(false)
                        storeBits(node.left.value.label.toBitString())
                    }.bits
                    newPrefix to node.left.value.node
                }
            }
        }
    }

    override fun computeNext() {
        val nextValue = gotoNext()
        if (nextValue != null) {
            setNext(nextValue)
        } else {
            done()
        }
    }

    private tailrec fun gotoNext(): Pair<BitString, T>? {
        val topState = state.firstOrNull() ?: return null
        val edge = topState.step()
        return if (edge == null) {
            state.removeFirst()
            gotoNext()
        } else {
            val (prefix, node) = edge
            if (node is HmnLeaf<T>) {
                prefix to node.value
            } else {
                addState(prefix, node)
                gotoNext()
            }
        }
    }
}

data class Trie<T>(
    var left: Trie<T>? = null,
    var right: Trie<T>? = null,
    var value: T? = null
) : Iterable<T> {
    operator fun set(key: String, value: T) = set(BitString.binary(key), value)

    operator fun set(key: BitString, value: T) {
        var x = this
        for (i in 0 until key.size) {
            x = if (key[i]) {
                x.right ?: Trie<T>().also {
                    x.right = it
                }
            } else {
                x.left ?: Trie<T>().also {
                    x.left = it
                }
            }
        }
        x.value = value
    }

    operator fun get(binary: String) = get(BitString.binary(binary))

    operator fun get(key: BitString, offset: Int = 0): T? {
        var x = this
        for (i in offset until key.size) {
            if (key[i]) {
                x.right?.also {
                    x = it
                } ?: break
            } else {
                x.left?.also {
                    x = it
                } ?: break
            }
        }
        return x.value
    }

    override fun iterator(): Iterator<T> {
        TODO()
    }
}
