package ton.fift

class Dictionary(
    val storage: MutableMap<String, WordDef> = HashMap(),
) : MutableMap<String, WordDef> by storage

operator fun Dictionary.set(name: String, isActive: Boolean = false, function: FiftInterpretator.() -> Unit) =
    set(name, object : WordDef {
        override val isActive: Boolean = isActive

        override fun execute(fift: FiftInterpretator) = function(fift)

        override fun toString(): String = name
    })

class WordList(
    list: MutableList<WordDef> = ArrayList(),
) : MutableList<WordDef> by list {
    override fun toString(): String = "WordList(${joinToString(",")})"
}

interface WordDef {
    val isActive: Boolean

    fun execute(fift: FiftInterpretator)
}

fun WordList.toWordDef() = WordListDef(this)

class WordListDef(
    val list: WordList,
) : WordDef {
    override val isActive: Boolean = false

    override fun execute(fift: FiftInterpretator) {
//        println("execute definition: $list")
        list.forEach { wordDef ->
//            println("   interpret: $wordDef")
            fift.interpret(wordDef)
        }
    }

    override fun toString(): String = "WordDef{${list.joinToString(",")}}"
}

object NopWordDef : WordDef {
    override val isActive: Boolean = false
    override fun execute(fift: FiftInterpretator) { /* nop. */
    }

    override fun toString(): String = "nop"
}