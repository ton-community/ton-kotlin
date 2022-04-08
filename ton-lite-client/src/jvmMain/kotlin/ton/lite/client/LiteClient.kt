package ton.lite.client

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import ton.adnl.AdnlClient
import ton.adnl.AdnlPublicKey
import ton.crypto.hex
import java.time.Instant

suspend fun main() = coroutineScope {
    val liteClient = LiteClient(
        host = "67.207.74.182",
        port = 4924,
        publicKey = hex("a5e253c3f6ab9517ecb204ee7fd04cca9273a8e8bb49712a48f496884c365353")
    ).connect()
    val time = liteClient.getTime()
    println("[server time: $time (${Instant.ofEpochSecond(time.now)})")

    val lastBlock = liteClient.getMasterchainInfo()
    println("last block: $lastBlock")
    val getAccountState = LiteServerGetAccountState(
        lastBlock.last,
        LiteServerAccountId(0, hex("0AB558F4DB84FD31F61A273535C670C091FFC619B1CDBBE5769A0BF28D3B8FEA"))
    )
    val a =
        hex(
            "250E896B" + // // LiteServerGetAccountState
                    "FFFFFFFF" + // TonNodeBlockId.id
                    "0000000000000080" + // TonNodeBlockId.shard
                    "DD052C01" + // TonNodeBlockId.sqno
                    "C8B050D60B1679BB0BF147709E11A06167F3A041245BE21BBF00B2DA1287787A" + // rootHash
                    "16F775B5B7057D88B4793CDC6921D144264FF328EFF0413059AFBB0E07FDA76B" + // fileHash
                    "00000000" + // LiteServerAccountId.workchain
                    "0AB558F4DB84FD31F61A273535C670C091FFC619B1CDBBE5769A0BF28D3B8FEA" // LiteServerAccountId.id
        )
    val answer =
        hex(
            "001100" +
                    "51c77970" + //
                    "ffffffff" + // TonNodeBlockId.id
                    "0000000000000080" + // TonNodeBlockId.shard
                    "fb082c01" + // TonNodeBlockId.sqno
                    "80421437d4572f260ebb9235be69fdc43f40d1d87dceb279d989431545d15a90bb40b340b4e8d00f3c9a4e4db996fab5b5ca249633f6eeeb3ac138d6fdb7f7e2000000000000000000000080c53b7a01e5caa7ebdda6c604f3931bb7c4ef76a34400da3d9415f08b8cf95d0da3a7a7b465a65a33aca4f1ffd6da4b579111ad3ac76e9381bac4ed5e35a9f895cc1e104efec60400b5ee9c72010219020004b90100094603257aac151f0ca9ddc4e271aff54fe11a42e4ca67ec32667e2c8ab7f73cef6834016e0209460380421437d4572f260ebb9235be69fdc43f40d1d87dceb279d989431545"
        )
//    val encodeBoxed = LiteServerGetAccountState.encodeBoxed(getAccountState)
//    println(hex(encodeBoxed))
//
    val accountState = liteClient.getAccountState(getAccountState)
    println(accountState)
}
// 041100 51c77970ffffffff000000000000008053092c01deaa899a97a4178065b3b0b68d090c049daceebb1e8473e34b0160d21560cba58f0c5cecf92b4b4036c532d75d644dcb051dfd58811fe232e76ae39fd0a0b52c000000000000000000000080223c7a01eea4c86e56124c2c728acf7e6c2b0ee9b7426556a3f51ee25c250fcd27c796825259bec572657da7f36f9b98f280755044bf72ec5deea76428b63393b8362356feca0400b5ee9c72010219020004bd01000946035d356c50dfb677ee4a917a191272c699e58af9f642ea7132349b5d3ee2b628e6016e02094603deaa899a97a4178065b3b0b68d090c049daceebb1e8473e34b0160d215
// 001100 51c77970ffffffff000000000000008059092c014036a8e3ebacbc43d94c516c2d7118e60c6789fea3f83a88f85fcfd5c55b0cbd9b5609157eb2a59b88cdf725680e80910c2610fa6b132d56ad47d2b95be614a8000000000000000000000080293c7a01761e08a11a85be139fa112a02a6255f499a8728e904c08e7c967a9b6384a0451374cebfb27801d3280e037f49937428fcb64ca288b0f937409f984d01cf7dadffec60400b5ee9c72010219020004b901000946034b2941c8a72391c3762b3516e53f5f067a7d8e969bf9d8f1dba795e0dff50798016e020946034036a8e3ebacbc43d94c516c2d7118e60c6789fea3f83a88f85fcfd5c5
// 041100 51c77970ffffffff00000000000000805c092c01b7f13fa6f37fa275fd3debfbb336cb73c78938793a8898416f521f85d01235ac982c0f557f91507523a9b1d7c972f2e45c580e4c2197a959c8ae5e791b86312d0000000000000000000000802c3c7a0176015718e8c24ff513ea53d2ebfe3e25df3e0947fea924006f62b8476de5970b885f83cebfbc75294958e4479b41e1e62f6bf81ae2c160e84ead5359f80a35a4feca0400b5ee9c72010219020004bd0100094603e2c18b2a2bb0280d32c7fbab17e31142a04250e2c512e16c6d27d6decd725929016e02094603b7f13fa6f37fa275fd3debfbb336cb73c78938793a8898416f521f85d0

private fun bits(hex: String): String = buildString {
    for (i in hex.indices step 2) {
        if (i + 1 <= hex.lastIndex) {
            append(hex[i + 1])
        }
        append(hex[i])
    }
}

suspend fun LiteClient.lastBlockTask() = coroutineScope {
    var lastBlock: LiteServerMasterchainInfo? = null
    while (isActive) {
        val currentBlock = getMasterchainInfo()
        if (currentBlock != lastBlock) {
            lastBlock = currentBlock
            println("[${Instant.now()}] $currentBlock")
        }
        delay(1000)
    }
}

class LiteClient(
    host: String,
    port: Int,
    publicKey: ByteArray,
) : LiteServerApi {
    override val adnlClient = AdnlClient(host, port, AdnlPublicKey(publicKey), Dispatchers.Default)

    suspend fun connect() = apply {
        adnlClient.connect()
    }
}