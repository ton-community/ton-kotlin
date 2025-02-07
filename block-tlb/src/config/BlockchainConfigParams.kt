package org.ton.kotlin.config

import org.ton.kotlin.bitstring.BitString
import org.ton.kotlin.cell.CellBuilder
import org.ton.kotlin.dict.RawDictionary
import org.ton.kotlin.message.address.StdAddr

/**
 * A non-empty dictionary with blockchain config params.
 */
public data class BlockchainConfigParams(
    public val dict: RawDictionary
) {
    /**
     * Config account address (in masterchain).
     *
     * ```tlb
     * _ config_addr:bits256 = ConfigParam 0;
     * ```
     */
    public var configAddress: StdAddr
        get() = StdAddr(-1, requireNotNull(dict[BitString("00000000")]).loadBitString(256))
        set(value) {
            dict[BitString("00000000")] = value.let {
                CellBuilder().storeBitString(it.address).toCellSlice()
            }
        }

    /**
     * Elector account address (in masterchain).
     *
     * ```tlb
     * _ elector_addr:bits256 = ConfigParam 1;
     * ```
     */
    public var electorAddress: StdAddr
        get() = StdAddr(-1, requireNotNull(dict[BitString("00000001")]).loadBitString(256))
        set(value) {
            dict[BitString("00000001")] = value.let {
                CellBuilder().storeBitString(it.address).toCellSlice()
            }
        }

    /**
     * Minter account address (in masterchain).
     *
     * NOTE: ConfigParam 0 is used if absent
     *
     * ```tlb
     * _ minter_addr:bits256 = ConfigParam 2;
     * ```
     */
    public var minterAddress: StdAddr
        get() = StdAddr(
            -1, requireNotNull(
                dict[BitString("00000002")] ?: dict[BitString("00000000")]
            ).loadBitString(256)
        )
        set(value) {
            dict[BitString("00000002")] = value.let {
                CellBuilder().storeBitString(it.address).toCellSlice()
            }
        }

    /**
     * Fee collector account address (in masterchain).
     *
     * NOTE: ConfigParam 1 is used if absent
     *
     * ```tlb
     * _ fee_collector_addr:bits256 = ConfigParam 3
     * ```
     */
    public var feeCollectorAddress: StdAddr
        get() = StdAddr(
            -1, requireNotNull(
                dict[BitString("00000003")] ?: dict[BitString("00000001")]
            ).loadBitString(256)
        )
        set(value) {
            dict[BitString("00000003")] = value.let {
                CellBuilder().storeBitString(it.address).toCellSlice()
            }
        }

    /**
     * DNS root resolver account address (in masterchain).
     *
     * ```tlb
     * _ dns_root_addr:bits256 = ConfigParam 4;
     * ```
     */
    public var dnsRootAddress: StdAddr
        get() = StdAddr(
            -1, requireNotNull(
                dict[BitString("00000004")]
            ).loadBitString(256)
        )
        set(value) {
            dict[BitString("00000004")] = value.let {
                CellBuilder().storeBitString(it.address).toCellSlice()
            }
        }

}