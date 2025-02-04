package org.ton.kotlin.benchmarks

import kotlinx.benchmark.*
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.dict.RawDictionary

@State(Scope.Benchmark)
open class DictBenchmark {
    @Param("520")
    var keysCount: Int = 0
    private lateinit var dict: RawDictionary
    private lateinit var value: CellSlice
    private lateinit var shuffled: List<BitString>

    @Setup
    fun setup() {
        dict = RawDictionary(32)
        value = CellBuilder().storeBoolean(true).build().beginParse()
    }

    @Benchmark
    fun benchmark() {
        shuffled = (0 until keysCount).shuffled().map { i ->
            CellBuilder().storeUInt(i.toUInt(), 32).toBitString()
        }
        shuffled.forEach { key ->
            dict.set(key, value)
        }
    }
}