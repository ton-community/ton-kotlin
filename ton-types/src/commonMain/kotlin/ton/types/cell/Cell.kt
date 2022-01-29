package ton.types.cell

data class Cell(
    val data: ByteArray = ByteArray(128),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Cell

        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        return data.contentHashCode()
    }

    fun hash(): ByteArray {
        TODO()
    }

    companion object {
        const val MAX_REFERENCES_COUNT = 4
        const val MAX_DATA_BITS = 1023
        const val MAX_LEVEL = 3
        const val MAX_DEPTH = Short.MAX_VALUE - 1
    }
}

enum class CellType {
    Unknown, Ordinary, PrunedBranch, LibraryReference, MerkleProof, MerkleUpdate
}