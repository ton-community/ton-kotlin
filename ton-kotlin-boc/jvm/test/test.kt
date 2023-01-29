import io.ktor.util.*
import org.ton.boc.BagOfCells
import org.ton.crypto.base64
import org.ton.crypto.encodeHex

fun main() {
    val bocBase64 =
        "te6ccuEBAQEASwCWAJIAAAAAQXVjdGlvbiBwcm9jZWVkcyBmb3IgdXNkdHh4eC50Lm1lIG1pbnVzIGNvbnZlcnNpb24gZmVlIGFuZCByb3lhbHRpZXMunEsqdg=="
    val boc = BagOfCells(base64(bocBase64))
    val cell = boc.first()
    println("cell: $cell")
    val newBoc = BagOfCells(cell)
    println("old boc: ${bocBase64.decodeBase64Bytes().encodeHex()}")
    println("new boc: ${newBoc.toByteArray().encodeHex()}")
}
