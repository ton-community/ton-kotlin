package org.ton.smartcontract.wallet

import org.ton.api.pk.PrivateKeyEd25519
import org.ton.crypto.base64
import org.ton.crypto.hex
import org.ton.lite.client.LiteClient
import org.ton.logger.Logger
import org.ton.logger.PrintLnLogger
import java.io.File

private val liteClient = LiteClient(
    ipv4 = 1426768764,
    port = 13724,
    publicKey = base64("R1KsqYlNks2Zows+I9s4ywhilbSevs9dH1x2KF9MeSU="),
    logger = PrintLnLogger("TON WalletV2", Logger.Level.DEBUG)
)

suspend fun main() {
    val pkFile = File("C:\\Users\\andreypfau\\ton\\wallet\\new-wallet.pk")
    val privateKey = PrivateKeyEd25519(pkFile.readBytes())
    val wallet = WalletV2R2(liteClient, privateKey)
    println("Public Key: ${hex(wallet.publicKey.key)}")
    val address = wallet.address()
    println("new wallet address = ${address.workchainId}:${hex(address.address)}")
    println("Non-bounceable address (for init only): ${address.toString(bounceable = false, testOnly = true)}")
    println("Bounceable address (for later access): ${address.toString(bounceable = true, testOnly = true)}")

    liteClient.connect()

    println(wallet.deploy())
}
