package ton.fift

class Dictionary(
    val storage: MutableMap<String, WordDef> = HashMap(),
) : MutableMap<String, WordDef> by storage

operator fun Dictionary.set(
    name: String,
    isActive: Boolean = false,
    function: FiftInterpretator.() -> Unit,
) = set(name, WordDef(isActive, function))

data class WordList(
    val list: MutableList<WordDef> = ArrayList(),
) : MutableList<WordDef> by list, WordDef {
    override val isActive: Boolean
        get() = false

    override fun execute(fift: FiftInterpretator) {
        list.forEach {
            it.execute(fift)
        }
    }
}

data class IterableWorldDef(
    val iterable: Iterable<WordDef>,
) : WordDef {
    override val isActive: Boolean = false

    override fun execute(fift: FiftInterpretator) {
        iterable.forEach {
            it.execute(fift)
        }
    }

    override fun toString(): String = "IterableWorldDef(${iterable.joinToString()})"
}

interface WordDef {
    val isActive: Boolean

    fun execute(fift: FiftInterpretator)
}

object NopWordDef : WordDef {
    override val isActive: Boolean = false
    override fun execute(fift: FiftInterpretator) {
        // nop.
    }

    override fun toString(): String = "nop"
}

fun WordDef(entry: Any) = object : WordDef {
    override val isActive: Boolean = false
    override fun execute(fift: FiftInterpretator) {
        fift.stack.push(entry)
    }
}

fun WordDef(isActive: Boolean = false, block: FiftInterpretator.() -> Unit): WordDef = object : WordDef {
    override val isActive: Boolean = isActive
    override fun execute(fift: FiftInterpretator) = block(fift)
}