package org.ton.kotlin.bitstring

internal actual fun ByteArray.setInt(index: Int, value: Int) {
    this[index] = (value ushr 24).toByte()
    this[index + 1] = (value ushr 16).toByte()
    this[index + 2] = (value ushr 8).toByte()
    this[index + 3] = value.toByte()
}

internal actual fun ByteArray.getInt(index: Int): Int {
    return (this[index].toInt() and 0xFF) shl 24 or
            (this[index + 1].toInt() and 0xFF) shl 16 or
            (this[index + 2].toInt() and 0xFF) shl 8 or
            (this[index + 3].toInt() and 0xFF)
}

internal actual fun ByteArray.setLong(index: Int, value: Long) {
    this[index] = (value shr 56).toByte()
    this[index + 1] = (value shr 48).toByte()
    this[index + 2] = (value shr 40).toByte()
    this[index + 3] = (value shr 32).toByte()
    this[index + 4] = (value shr 24).toByte()
    this[index + 5] = (value shr 16).toByte()
    this[index + 6] = (value shr 8).toByte()
    this[index + 7] = value.toByte()
}

//@OptIn(ExperimentalNativeApi::class)
//internal actual fun ByteArray.setInt(index: Int, value: Int) {
//    this.setIntAt(index, value.reverseBytes())
//}
//
//@OptIn(ExperimentalNativeApi::class)
//internal actual fun ByteArray.getInt(index: Int): Int {
//    return getIntAt(index).reverseBytes()
//}
//
//@OptIn(ExperimentalNativeApi::class)
//internal actual fun ByteArray.setLong(index: Int, value: Long) {
//    this.setLongAt(index, value.reverseBytes())
//}
//
//@Suppress("NOTHING_TO_INLINE")
//private inline fun Int.reverseBytes(): Int {
//    return (this shl 24) or
//            ((this and 0xff00) shl 8) or
//            ((this ushr 8) and 0xff00) or
//            (this ushr 24)
//}
//
//@Suppress("NOTHING_TO_INLINE")
//private inline fun Long.reverseBytes(): Long {
//    var i = this
//    i = (i and 0x00ff00ff00ff00ffL) shl 8 or ((i ushr 8) and 0x00ff00ff00ff00ffL)
//    return (i shl 48) or ((i and 0xffff0000L) shl 16) or
//            ((i ushr 16) and 0xffff0000L) or (i ushr 48)
//}
// DictBenchmark.benchmark          520  thrpt    5  372.886 ± 5.676  ops/sec
// DictBenchmark.benchmark          520  thrpt    5  366.573 ± 8.298  ops/sec
// DictBenchmark.benchmark          520  thrpt    5  377.418 ± 3.125  ops/sec

// DictBenchmark.benchmark          520  thrpt    5  368.184 ± 1.909  ops/sec