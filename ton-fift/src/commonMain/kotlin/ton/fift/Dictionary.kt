package ton.fift

class Dictionary(
    val storage: MutableMap<String, WordDef> = HashMap(),
) : MutableMap<String, WordDef> by storage

inline operator fun Dictionary.set(
    name: String,
    isActive: Boolean = false,
    crossinline function: (FiftInterpretator) -> Unit,
) =
    set(name, object : WordDef {
        override val isActive: Boolean = isActive

        override fun execute(fift: FiftInterpretator) = function(fift)

        override fun toString(): String = name
    })

data class WordList(
    val list: MutableList<WordDef> = ArrayList(),
) : MutableList<WordDef> by list, WordDef {
    override val isActive: Boolean
        get() = false

    override fun execute(fift: FiftInterpretator) {
        println(toString())
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

    override fun toString(): String = "IterableWorldDef$iterable"
}

data class PushStackWordDef(
    val value: Any,
) : WordDef {
    override val isActive: Boolean = false
    override fun execute(fift: FiftInterpretator) {
        fift.stack.push(value)
    }

    override fun toString(): String = "PushStackWordDef(value=$value)"
}

data class FunctionWordDef(
    val function: (FiftInterpretator) -> Unit,
) : WordDef {
    override val isActive: Boolean = false
    override fun execute(fift: FiftInterpretator) {
        function(fift)
    }

    override fun toString(): String = "FunctionWordDef(function=$function)"
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