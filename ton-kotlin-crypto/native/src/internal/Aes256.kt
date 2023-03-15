package internal

import kotlin.experimental.xor

// The S box
private val S = ubyteArrayOf(
    0x63u, 0x7cu, 0x77u, 0x7bu, 0xf2u, 0x6bu, 0x6fu, 0xc5u, 0x30u, 0x01u, 0x67u, 0x2bu,
    0xfeu, 0xd7u, 0xabu, 0x76u, 0xcau, 0x82u, 0xc9u, 0x7du, 0xfau, 0x59u, 0x47u, 0xf0u,
    0xadu, 0xd4u, 0xa2u, 0xafu, 0x9cu, 0xa4u, 0x72u, 0xc0u, 0xb7u, 0xfdu, 0x93u, 0x26u,
    0x36u, 0x3fu, 0xf7u, 0xccu, 0x34u, 0xa5u, 0xe5u, 0xf1u, 0x71u, 0xd8u, 0x31u, 0x15u,
    0x04u, 0xc7u, 0x23u, 0xc3u, 0x18u, 0x96u, 0x05u, 0x9au, 0x07u, 0x12u, 0x80u, 0xe2u,
    0xebu, 0x27u, 0xb2u, 0x75u, 0x09u, 0x83u, 0x2cu, 0x1au, 0x1bu, 0x6eu, 0x5au, 0xa0u,
    0x52u, 0x3bu, 0xd6u, 0xb3u, 0x29u, 0xe3u, 0x2fu, 0x84u, 0x53u, 0xd1u, 0x00u, 0xedu,
    0x20u, 0xfcu, 0xb1u, 0x5bu, 0x6au, 0xcbu, 0xbeu, 0x39u, 0x4au, 0x4cu, 0x58u, 0xcfu,
    0xd0u, 0xefu, 0xaau, 0xfbu, 0x43u, 0x4du, 0x33u, 0x85u, 0x45u, 0xf9u, 0x02u, 0x7fu,
    0x50u, 0x3cu, 0x9fu, 0xa8u, 0x51u, 0xa3u, 0x40u, 0x8fu, 0x92u, 0x9du, 0x38u, 0xf5u,
    0xbcu, 0xb6u, 0xdau, 0x21u, 0x10u, 0xffu, 0xf3u, 0xd2u, 0xcdu, 0x0cu, 0x13u, 0xecu,
    0x5fu, 0x97u, 0x44u, 0x17u, 0xc4u, 0xa7u, 0x7eu, 0x3du, 0x64u, 0x5du, 0x19u, 0x73u,
    0x60u, 0x81u, 0x4fu, 0xdcu, 0x22u, 0x2au, 0x90u, 0x88u, 0x46u, 0xeeu, 0xb8u, 0x14u,
    0xdeu, 0x5eu, 0x0bu, 0xdbu, 0xe0u, 0x32u, 0x3au, 0x0au, 0x49u, 0x06u, 0x24u, 0x5cu,
    0xc2u, 0xd3u, 0xacu, 0x62u, 0x91u, 0x95u, 0xe4u, 0x79u, 0xe7u, 0xc8u, 0x37u, 0x6du,
    0x8du, 0xd5u, 0x4eu, 0xa9u, 0x6cu, 0x56u, 0xf4u, 0xeau, 0x65u, 0x7au, 0xaeu, 0x08u,
    0xbau, 0x78u, 0x25u, 0x2eu, 0x1cu, 0xa6u, 0xb4u, 0xc6u, 0xe8u, 0xddu, 0x74u, 0x1fu,
    0x4bu, 0xbdu, 0x8bu, 0x8au, 0x70u, 0x3eu, 0xb5u, 0x66u, 0x48u, 0x03u, 0xf6u, 0x0eu,
    0x61u, 0x35u, 0x57u, 0xb9u, 0x86u, 0xc1u, 0x1du, 0x9eu, 0xe1u, 0xf8u, 0x98u, 0x11u,
    0x69u, 0xd9u, 0x8eu, 0x94u, 0x9bu, 0x1eu, 0x87u, 0xe9u, 0xceu, 0x55u, 0x28u, 0xdfu,
    0x8cu, 0xa1u, 0x89u, 0x0du, 0xbfu, 0xe6u, 0x42u, 0x68u, 0x41u, 0x99u, 0x2du, 0x0fu,
    0xb0u, 0x54u, 0xbbu, 0x16u
).toByteArray()

// precomputation tables of calculations for rounds
private val T0 = uintArrayOf(
    0xa56363c6u, 0x847c7cf8u, 0x997777eeu, 0x8d7b7bf6u, 0x0df2f2ffu,
    0xbd6b6bd6u, 0xb16f6fdeu, 0x54c5c591u, 0x50303060u, 0x03010102u,
    0xa96767ceu, 0x7d2b2b56u, 0x19fefee7u, 0x62d7d7b5u, 0xe6abab4du,
    0x9a7676ecu, 0x45caca8fu, 0x9d82821fu, 0x40c9c989u, 0x877d7dfau,
    0x15fafaefu, 0xeb5959b2u, 0xc947478eu, 0x0bf0f0fbu, 0xecadad41u,
    0x67d4d4b3u, 0xfda2a25fu, 0xeaafaf45u, 0xbf9c9c23u, 0xf7a4a453u,
    0x967272e4u, 0x5bc0c09bu, 0xc2b7b775u, 0x1cfdfde1u, 0xae93933du,
    0x6a26264cu, 0x5a36366cu, 0x413f3f7eu, 0x02f7f7f5u, 0x4fcccc83u,
    0x5c343468u, 0xf4a5a551u, 0x34e5e5d1u, 0x08f1f1f9u, 0x937171e2u,
    0x73d8d8abu, 0x53313162u, 0x3f15152au, 0x0c040408u, 0x52c7c795u,
    0x65232346u, 0x5ec3c39du, 0x28181830u, 0xa1969637u, 0x0f05050au,
    0xb59a9a2fu, 0x0907070eu, 0x36121224u, 0x9b80801bu, 0x3de2e2dfu,
    0x26ebebcdu, 0x6927274eu, 0xcdb2b27fu, 0x9f7575eau, 0x1b090912u,
    0x9e83831du, 0x742c2c58u, 0x2e1a1a34u, 0x2d1b1b36u, 0xb26e6edcu,
    0xee5a5ab4u, 0xfba0a05bu, 0xf65252a4u, 0x4d3b3b76u, 0x61d6d6b7u,
    0xceb3b37du, 0x7b292952u, 0x3ee3e3ddu, 0x712f2f5eu, 0x97848413u,
    0xf55353a6u, 0x68d1d1b9u, 0x00000000u, 0x2cededc1u, 0x60202040u,
    0x1ffcfce3u, 0xc8b1b179u, 0xed5b5bb6u, 0xbe6a6ad4u, 0x46cbcb8du,
    0xd9bebe67u, 0x4b393972u, 0xde4a4a94u, 0xd44c4c98u, 0xe85858b0u,
    0x4acfcf85u, 0x6bd0d0bbu, 0x2aefefc5u, 0xe5aaaa4fu, 0x16fbfbedu,
    0xc5434386u, 0xd74d4d9au, 0x55333366u, 0x94858511u, 0xcf45458au,
    0x10f9f9e9u, 0x06020204u, 0x817f7ffeu, 0xf05050a0u, 0x443c3c78u,
    0xba9f9f25u, 0xe3a8a84bu, 0xf35151a2u, 0xfea3a35du, 0xc0404080u,
    0x8a8f8f05u, 0xad92923fu, 0xbc9d9d21u, 0x48383870u, 0x04f5f5f1u,
    0xdfbcbc63u, 0xc1b6b677u, 0x75dadaafu, 0x63212142u, 0x30101020u,
    0x1affffe5u, 0x0ef3f3fdu, 0x6dd2d2bfu, 0x4ccdcd81u, 0x140c0c18u,
    0x35131326u, 0x2fececc3u, 0xe15f5fbeu, 0xa2979735u, 0xcc444488u,
    0x3917172eu, 0x57c4c493u, 0xf2a7a755u, 0x827e7efcu, 0x473d3d7au,
    0xac6464c8u, 0xe75d5dbau, 0x2b191932u, 0x957373e6u, 0xa06060c0u,
    0x98818119u, 0xd14f4f9eu, 0x7fdcdca3u, 0x66222244u, 0x7e2a2a54u,
    0xab90903bu, 0x8388880bu, 0xca46468cu, 0x29eeeec7u, 0xd3b8b86bu,
    0x3c141428u, 0x79dedea7u, 0xe25e5ebcu, 0x1d0b0b16u, 0x76dbdbadu,
    0x3be0e0dbu, 0x56323264u, 0x4e3a3a74u, 0x1e0a0a14u, 0xdb494992u,
    0x0a06060cu, 0x6c242448u, 0xe45c5cb8u, 0x5dc2c29fu, 0x6ed3d3bdu,
    0xefacac43u, 0xa66262c4u, 0xa8919139u, 0xa4959531u, 0x37e4e4d3u,
    0x8b7979f2u, 0x32e7e7d5u, 0x43c8c88bu, 0x5937376eu, 0xb76d6ddau,
    0x8c8d8d01u, 0x64d5d5b1u, 0xd24e4e9cu, 0xe0a9a949u, 0xb46c6cd8u,
    0xfa5656acu, 0x07f4f4f3u, 0x25eaeacfu, 0xaf6565cau, 0x8e7a7af4u,
    0xe9aeae47u, 0x18080810u, 0xd5baba6fu, 0x887878f0u, 0x6f25254au,
    0x722e2e5cu, 0x241c1c38u, 0xf1a6a657u, 0xc7b4b473u, 0x51c6c697u,
    0x23e8e8cbu, 0x7cdddda1u, 0x9c7474e8u, 0x211f1f3eu, 0xdd4b4b96u,
    0xdcbdbd61u, 0x868b8b0du, 0x858a8a0fu, 0x907070e0u, 0x423e3e7cu,
    0xc4b5b571u, 0xaa6666ccu, 0xd8484890u, 0x05030306u, 0x01f6f6f7u,
    0x120e0e1cu, 0xa36161c2u, 0x5f35356au, 0xf95757aeu, 0xd0b9b969u,
    0x91868617u, 0x58c1c199u, 0x271d1d3au, 0xb99e9e27u, 0x38e1e1d9u,
    0x13f8f8ebu, 0xb398982bu, 0x33111122u, 0xbb6969d2u, 0x70d9d9a9u,
    0x898e8e07u, 0xa7949433u, 0xb69b9b2du, 0x221e1e3cu, 0x92878715u,
    0x20e9e9c9u, 0x49cece87u, 0xff5555aau, 0x78282850u, 0x7adfdfa5u,
    0x8f8c8c03u, 0xf8a1a159u, 0x80898909u, 0x170d0d1au, 0xdabfbf65u,
    0x31e6e6d7u, 0xc6424284u, 0xb86868d0u, 0xc3414182u, 0xb0999929u,
    0x772d2d5au, 0x110f0f1eu, 0xcbb0b07bu, 0xfc5454a8u, 0xd6bbbb6du,
    0x3a16162cu,
).toIntArray()

internal abstract class Aes256Impl constructor(
    key: ByteArray
) {
    private val wk = generateWorkingKey(key)

    fun encryptBlock(
        input: ByteArray,
        inputOffset: Int,
        output: ByteArray,
        outputOffset: Int,
    ): ByteArray {
        var c0 = input.getIntLE(inputOffset + 0)
        var c1 = input.getIntLE(inputOffset + 4)
        var c2 = input.getIntLE(inputOffset + 8)
        var c3 = input.getIntLE(inputOffset + 12)

        var t0 = c0 xor wk[0][0]
        var t1 = c1 xor wk[0][1]
        var t2 = c2 xor wk[0][2]

        var r = 1
        var r0: Int
        var r1: Int
        var r2: Int
        var r3 = c3 xor wk[0][3]

        while (r < ROUNDS - 1) {
            r0 = T0[t0 and 0xFF] xor shift(T0[(t1 ushr 8) and 0xFF], 24) xor
                    shift(T0[(t2 ushr 16) and 0xFF], 16) xor
                    shift(T0[(r3 ushr 24) and 0xFF], 8) xor wk[r][0]
            r1 = T0[t1 and 0xFF] xor shift(T0[(t2 ushr 8) and 0xFF], 24) xor
                    shift(T0[(r3 ushr 16) and 0xFF], 16) xor
                    shift(T0[(t0 ushr 24) and 0xFF], 8) xor wk[r][1]
            r2 = T0[t2 and 0xFF] xor shift(T0[(r3 ushr 8) and 0xFF], 24) xor
                    shift(T0[(t0 ushr 16) and 0xFF], 16) xor
                    shift(T0[(t1 ushr 24) and 0xFF], 8) xor wk[r][2]
            r3 = T0[r3 and 0xFF] xor shift(T0[(t0 ushr 8) and 0xFF], 24) xor
                    shift(T0[(t1 ushr 16) and 0xFF], 16) xor
                    shift(T0[(t2 ushr 24) and 0xFF], 8) xor wk[r][3]
            r++
            t0 = T0[r0 and 0xFF] xor shift(T0[(r1 ushr 8) and 0xFF], 24) xor
                    shift(T0[(r2 ushr 16) and 0xFF], 16) xor
                    shift(T0[(r3 ushr 24) and 0xFF], 8) xor wk[r][0]
            t1 = T0[r1 and 0xFF] xor shift(T0[(r2 ushr 8) and 0xFF], 24) xor
                    shift(T0[(r3 ushr 16) and 0xFF], 16) xor
                    shift(T0[(r0 ushr 24) and 0xFF], 8) xor wk[r][1]
            t2 = T0[r2 and 0xFF] xor shift(T0[(r3 ushr 8) and 0xFF], 24) xor
                    shift(T0[(r0 ushr 16) and 0xFF], 16) xor
                    shift(T0[(r1 ushr 24) and 0xFF], 8) xor wk[r][2]
            r3 = T0[r3 and 0xFF] xor shift(T0[(r0 ushr 8) and 0xFF], 24) xor
                    shift(T0[(r1 ushr 16) and 0xFF], 16) xor
                    shift(T0[(r2 ushr 24) and 0xFF], 8) xor wk[r][3]
            r++
        }

        r0 = T0[t0 and 0xFF] xor shift(T0[(t1 ushr 8) and 0xFF], 24) xor
                shift(T0[(t2 ushr 16) and 0xFF], 16) xor
                shift(T0[(r3 ushr 24) and 0xFF], 8) xor wk[r][0]
        r1 = T0[t1 and 0xFF] xor shift(T0[(t2 ushr 8) and 0xFF], 24) xor
                shift(T0[(r3 ushr 16) and 0xFF], 16) xor
                shift(T0[(t0 ushr 24) and 0xFF], 8) xor wk[r][1]
        r2 = T0[t2 and 0xFF] xor shift(T0[(r3 ushr 8) and 0xFF], 24) xor
                shift(T0[(t0 ushr 16) and 0xFF], 16) xor
                shift(T0[(t1 ushr 24) and 0xFF], 8) xor wk[r][2]
        r3 = T0[r3 and 0xFF] xor shift(T0[(t0 ushr 8) and 0xFF], 24) xor
                shift(T0[(t1 ushr 16) and 0xFF], 16) xor
                shift(T0[(t2 ushr 24) and 0xFF], 8) xor wk[r][3]

        // the final round's table is a simple function of S, so we don't use a whole other four tables for it

        c0 = (S[r0 and 0xFF].toInt() and 0xFF) xor ((S[(r1 shr 8) and 0xFF].toInt() and 0xFF) shl 8) xor
                ((S[(r2 shr 16) and 0xFF].toInt() and 0xFF) shl 16) xor
                (S[(r3 shr 24) and 0xFF].toInt() shl 24) xor wk[r + 1][0]
        c1 = (S[r1 and 0xFF].toInt() and 0xFF) xor ((S[(r2 shr 8) and 0xFF].toInt() and 0xFF) shl 8) xor
                ((S[(r3 shr 16) and 0xFF].toInt() and 0xFF) shl 16) xor
                (S[(r0 shr 24) and 0xFF].toInt() shl 24) xor wk[r + 1][1]
        c2 = (S[r2 and 0xFF].toInt() and 0xFF) xor ((S[(r3 shr 8) and 0xFF].toInt() and 0xFF) shl 8) xor
                ((S[(r0 shr 16) and 0xFF].toInt() and 0xFF) shl 16) xor
                (S[(r1 shr 24) and 0xFF].toInt() shl 24) xor wk[r + 1][2]
        c3 = (S[r3 and 0xFF].toInt() and 0xFF) xor ((S[(r0 shr 8) and 0xFF].toInt() and 0xFF) shl 8) xor
                ((S[(r1 shr 16) and 0xFF].toInt() and 0xFF) shl 16) xor
                (S[(r2 shr 24) and 0xFF].toInt() shl 24) xor wk[r + 1][3]

        output.setIntLE(outputOffset + 0, c0)
        output.setIntLE(outputOffset + 4, c1)
        output.setIntLE(outputOffset + 8, c2)
        output.setIntLE(outputOffset + 12, c3)

        return output
    }

    companion object {
        const val KC = 8
        const val ROUNDS = KC + 6
        const val BLOCK_SIZE = 16
    }
}

private fun generateWorkingKey(
    key: ByteArray
): Array<IntArray> {
    val keyLen = key.size
    require(keyLen == 32) { "Key length must be 256 bits." }
    val w = Array(Aes256Impl.ROUNDS + 1) { IntArray(4) }

    var col0 = key.getIntLE(0)
    var col1 = key.getIntLE(4)
    var col2 = key.getIntLE(8)
    var col3 = key.getIntLE(12)
    w[0][0] = col0
    w[0][1] = col1
    w[0][2] = col2
    w[0][3] = col3

    var col4 = key.getIntLE(16)
    var col5 = key.getIntLE(20)
    var col6 = key.getIntLE(24)
    var col7 = key.getIntLE(28)
    w[1][0] = col4
    w[1][1] = col5
    w[1][2] = col6
    w[1][3] = col7

    var i = 2
    var rcon = 1
    var colx: Int
    while (true) {
        colx = subWord(shift(col7, 8)) xor rcon
        rcon = rcon shl 1
        col0 = col0 xor colx
        col1 = col1 xor col0
        col2 = col2 xor col1
        col3 = col3 xor col2
        w[i][0] = col0
        w[i][1] = col1
        w[i][2] = col2
        w[i][3] = col3

        i++
        if (i >= 15) break

        colx = subWord(col3)
        col4 = col4 xor colx
        col5 = col5 xor col4
        col6 = col6 xor col5
        col7 = col7 xor col6
        w[i][0] = col4
        w[i][1] = col5
        w[i][2] = col6
        w[i][3] = col7
        i++
    }
    return w
}

private fun subWord(x: Int) =
    (S[x and 0xFF].toInt() and 0xFF) or
            ((S[x ushr 8 and 0xFF].toInt() and 0xFF) shl 8) or
            ((S[x ushr 16 and 0xFF].toInt() and 0xFF) shl 16) or
            ((S[x ushr 24 and 0xFF].toInt() and 0xFF) shl 24)

private fun shift(r: Int, shift: Int) = (r ushr shift) or (r shl -shift)

private fun ByteArray.getIntLE(index: Int): Int {
    return (this[index].toInt() and 0xFF) or
            ((this[index + 1].toInt() and 0xFF) shl 8) or
            ((this[index + 2].toInt() and 0xFF) shl 16) or
            ((this[index + 3].toInt() and 0xFF) shl 24)
}

private fun ByteArray.setIntLE(index: Int, value: Int) {
    this[index] = value.toByte()
    this[index + 1] = (value ushr 8).toByte()
    this[index + 2] = (value ushr 16).toByte()
    this[index + 3] = (value ushr 24).toByte()
}

internal class AesCtr256Impl(
    key: ByteArray,
    iv: ByteArray
) : Aes256Impl(key) {
    private val iv = iv.copyOf(16)
    private var counter = BLOCK_SIZE
    private val buffer = ByteArray(BLOCK_SIZE)

    fun encrypt(input: ByteArray, output: ByteArray): ByteArray {
        for (i in input.indices) {
            if (counter == BLOCK_SIZE) {
                iv.copyInto(buffer)
                encryptBlock(buffer, 0, buffer, 0)

                for (j in BLOCK_SIZE - 1 downTo 0) {
                    if (iv[j] == 0xFF.toByte()) {
                        iv[j] = 0
                    } else {
                        iv[j]++
                        break
                    }
                }
                counter = 0
            }
            output[i] = input[i] xor buffer[counter]
            counter++
        }
        return output
    }
}
