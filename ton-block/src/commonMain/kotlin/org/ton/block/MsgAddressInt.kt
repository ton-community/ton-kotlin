package org.ton.block

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.bigint.toBigInt
import org.ton.bitstring.BitString
import org.ton.bitstring.toBitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.crypto.base64
import org.ton.crypto.base64url
import org.ton.crypto.crc16
import org.ton.crypto.hex
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.experimental.and
import kotlin.experimental.or

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("@type")
@Serializable
sealed interface MsgAddressInt : MsgAddress {
    val workchainId: Int

    companion object {
        @JvmStatic
        fun tlbCodec(): TlbCombinator<MsgAddressInt> = MsgAddressIntTlbCombinator

        @JvmStatic
        fun toString(
            address: MsgAddressInt,
            userFriendly: Boolean = true,
            urlSafe: Boolean = true,
            testOnly: Boolean = false,
            bounceable: Boolean = true
        ): String {
            checkAddressStd(address)
            return MsgAddressIntStd.toString(address, userFriendly, urlSafe, testOnly, bounceable)
        }

        @JvmStatic
        fun parse(address: String): MsgAddressInt = MsgAddressIntStd.parse(address)

        @JvmStatic
        fun parseRaw(address: String): MsgAddressInt = MsgAddressIntStd.parseRaw(address)

        @JvmStatic
        fun parseUserFriendly(address: String): MsgAddressInt = MsgAddressIntStd.parseUserFriendly(address)

        @OptIn(ExperimentalContracts::class)
        private fun checkAddressStd(value: MsgAddressInt) {
            contract {
                returns() implies (value is MsgAddressIntStd)
            }
            require(value is MsgAddressIntStd) {
                "expected class: ${MsgAddressIntStd::class} actual: ${value::class}"
            }
        }
    }

    private object MsgAddressIntTlbCombinator : TlbCombinator<MsgAddressInt>() {
        private val addrStdConstructor by lazy {
            MsgAddressIntStd.tlbCodec()
        }
        private val addrVarConstructor by lazy {
            MsgAddressIntVar.tlbCodec()
        }

        override val constructors: List<TlbConstructor<out MsgAddressInt>> by lazy {
            listOf(addrStdConstructor, addrVarConstructor)
        }

        override fun getConstructor(value: MsgAddressInt): TlbConstructor<out MsgAddressInt> = when (value) {
            is MsgAddressIntStd -> addrStdConstructor
            is MsgAddressIntVar -> addrVarConstructor
        }
    }
}


@SerialName("addr_std")
@Serializable
data class MsgAddressIntStd(
    val anycast: Maybe<Anycast>,
    @SerialName("workchain_id")
    override val workchainId: Int,
    val address: BitString
) : MsgAddressInt {
    init {
        require(address.size == 256) { "address.size expected: 256 actual: ${address.size}" }
    }

    constructor(workchainId: Int, address: BitString) : this(null, workchainId, address)
    constructor(workchainId: Int, address: ByteArray) : this(null, workchainId, address)
    constructor(anycast: Anycast?, workchainId: Int, address: ByteArray) : this(
        anycast.toMaybe(),
        workchainId,
        address.toBitString()
    )

    constructor(anycast: Anycast?, workchainId: Int, address: BitString) : this(
        anycast.toMaybe(),
        workchainId,
        address
    )

    fun toString(
        userFriendly: Boolean = true,
        urlSafe: Boolean = true,
        testOnly: Boolean = false,
        bounceable: Boolean = true
    ): String = toString(this, userFriendly, urlSafe, testOnly, bounceable)

    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<MsgAddressIntStd> = AddrStdTlbConstructor

        @JvmStatic
        fun toString(
            address: MsgAddressIntStd,
            userFriendly: Boolean = true,
            urlSafe: Boolean = true,
            testOnly: Boolean = false,
            bounceable: Boolean = true
        ): String {
            return if (userFriendly) {
                val raw = byteArrayOf(tag(testOnly, bounceable), address.workchainId.toByte()) +
                        address.address.toByteArray() + crc(address, testOnly, bounceable).toShort().toBigInt()
                    .toByteArray()
                if (urlSafe) {
                    base64url(raw)
                } else {
                    base64(raw)
                }
            } else {
                address.workchainId.toString() + ":" + hex(address.address.toByteArray())
            }
        }

        @JvmStatic
        fun parse(address: String): MsgAddressIntStd {
            return if (address.contains(':')) {
                parseRaw(address)
            } else {
                parseUserFriendly(address)
            }
        }

        @JvmStatic
        fun parseRaw(address: String): MsgAddressIntStd {
            require(address.contains(':'))
            // 32 bytes, each represented as 2 characters
            require(address.substringAfter(':').length == 32 * 2)
            return MsgAddressIntStd(
                // toByte() to make sure it fits into 8 bits
                workchainId = address.substringBefore(':').toByte().toInt(),
                address = hex(address.substringAfter(':'))
            )
        }

        @JvmStatic
        fun parseUserFriendly(address: String): MsgAddressIntStd {
            val raw = try {
                base64url(address)
            } catch (E: Exception) {
                base64(address)
            }

            require(raw.size == 36)
            return MsgAddressIntStd(
                workchainId = raw[1].toInt(),
                address = raw.sliceArray(2..33)
            ).apply {
                val testOnly = raw[0] and 0x80.toByte() != 0.toByte()
                if (testOnly) {
                    // not 0x80 = 0x7F; here we clean the test only flag
                    raw[0] = raw[0] and 0x7F.toByte()
                }

                check((raw[0] == 0x11.toByte()) or (raw[0] == 0x51.toByte())) { "unknown address tag" }

                val bounceable = raw[0] == 0x11.toByte()
                check(
                    (crc(
                        this,
                        testOnly,
                        bounceable
                    ) == raw[34].toUByte().toInt() * 256 + raw[35].toUByte().toInt())
                ) { "CRC check failed" }
            }
        }

        private fun crc(address: MsgAddressIntStd, testOnly: Boolean, bounceable: Boolean): Int =
            crc16(
                byteArrayOf(tag(testOnly, bounceable), address.workchainId.toByte()),
                address.address.toByteArray()
            )

        // Get the tag byte based on set flags
        private fun tag(testOnly: Boolean, bounceable: Boolean): Byte =
            (if (testOnly) 0x80.toByte() else 0.toByte()) or
                    (if (bounceable) 0x11.toByte() else 0x51.toByte())
    }

    private object AddrStdTlbConstructor : TlbConstructor<MsgAddressIntStd>(
        schema = "addr_std\$10 anycast:(Maybe Anycast) workchain_id:int8 address:bits256 = MsgAddressInt;"
    ) {
        private val maybeAnycastCodec by lazy {
            Maybe.tlbCodec(Anycast.tlbCodec())
        }

        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: MsgAddressIntStd
        ) = cellBuilder {
            storeTlb(maybeAnycastCodec, value.anycast)
            storeInt(value.workchainId, 8)
            storeBits(value.address)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): MsgAddressIntStd = cellSlice {
            val anycast = loadTlb(maybeAnycastCodec)
            val workchainId = loadInt(8).toInt()
            val address = loadBitString(256)
            MsgAddressIntStd(anycast, workchainId, address)
        }
    }
}

@SerialName("addr_var")
@Serializable
data class MsgAddressIntVar(
    val anycast: Maybe<Anycast>,
    val addrLen: Int,
    override val workchainId: Int,
    val address: BitString
) : MsgAddressInt {
    init {
        require(address.size == addrLen) { "address.size expected: $addrLen actual: ${address.size}" }
    }

    constructor(workchainId: Int, address: ByteArray) : this(null, workchainId, address)
    constructor(workchainId: Int, address: BitString) : this(null, workchainId, address)
    constructor(anycast: Anycast?, workchainId: Int, address: BitString) : this(
        anycast.toMaybe(),
        address.size,
        workchainId,
        address
    )

    constructor(anycast: Anycast?, workchainId: Int, address: ByteArray) : this(
        anycast.toMaybe(),
        address.size,
        workchainId,
        address.toBitString()
    )

    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<MsgAddressIntVar> = AddrVarTlbConstructor
    }

    private object AddrVarTlbConstructor : TlbConstructor<MsgAddressIntVar>(
        schema = "addr_var\$11 anycast:(Maybe Anycast) addr_len:(## 9) workchain_id:int32 address:(bits addr_len) = MsgAddressInt;"
    ) {
        private val maybeAnycastCodec by lazy {
            Maybe.tlbCodec(Anycast.tlbCodec())
        }

        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: MsgAddressIntVar
        ) = cellBuilder {
            storeTlb(maybeAnycastCodec, value.anycast)
            storeUInt(value.addrLen, 9)
            storeInt(value.workchainId, 32)
            storeBits(value.address)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): MsgAddressIntVar = cellSlice {
            val anycast = loadTlb(maybeAnycastCodec)
            val addrLen = loadUInt(9).toInt()
            val workchainId = loadInt(32).toInt()
            val address = loadBitString(addrLen)
            MsgAddressIntVar(anycast, addrLen, workchainId, address)
        }
    }
}
