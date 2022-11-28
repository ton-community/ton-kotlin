package org.ton.experimental

//    @Test
//    fun encoder() {
//        val data = Random.Default.nextBytes(10_000)
//        val raptorQFecEncoder = RaptorQFecEncoder(data)
//        var seqno = 0
//        println("symbol count: ${raptorQFecEncoder.fecType.symbol_count}")
//        val packets: Array<ByteArray?> = Array(raptorQFecEncoder.fecType.symbol_count + 50) {
//            val symbol = raptorQFecEncoder.encode(seqno)
//            println("seqno: $seqno, symbol: ${symbol.encodeHex()}")
//            seqno++
//            symbol
//        }
//        // remove 5 random packets from array
//        for (i in 0 until 10) {
//            packets[Random.Default.nextInt(packets.size)] = null
//        }
//
//        val decoder = RaptorQFecDecoder(raptorQFecEncoder.fecType)
//
//        var result: ByteArray? = null
//        for (i in packets.indices) {
//            val packet = packets[i]
//            if (packet != null) {
//                println("decoding: $i")
//                result = decoder.decode(i, packet)
//                if (result != null) {
//                    println("success")
//                    break
//                }
//            }
//        }
//        println(data.encodeHex().chunked(1000).joinToString("\n"))
//        println("  ")
//        println(result?.encodeHex()?.chunked(1000)?.joinToString("\n"))
//        assertContentEquals(data, assertNotNull(result))
//    }
//}
