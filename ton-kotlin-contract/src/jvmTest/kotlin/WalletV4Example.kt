import org.ton.api.pk.PrivateKeyEd25519
import org.ton.bitstring.BitString
import org.ton.block.AddrStd
import org.ton.block.Coins
import org.ton.contract.wallet.WalletTransfer
import org.ton.contract.wallet.WalletV4R2Contract
import org.ton.contract.wallet.liteClient
import org.ton.mnemonic.Mnemonic
import java.io.File

suspend fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Usage: <path-to-mnemonics>")
        return
    }
    val mnemonics = File(args[0]).readLines()

    val liteApi = liteClient().liteApi
    val privateKey =
        PrivateKeyEd25519(Mnemonic.toSeed(mnemonics))

    val wallet = WalletV4R2Contract(0,privateKey.publicKey())
    println(wallet.address)
    wallet.transfer(liteApi, privateKey, WalletTransfer {
        destination = AddrStd(0, BitString("0ab558f4db84fd31f61a273535c670c091ffc619b1cdbbe5769a0bf28d3b8fea"))
        coins = Coins.ofNano(2)
    }, WalletTransfer {
        destination = AddrStd(0, BitString("0000000000000000000000000000000000000000000000000000000000000000"))
        coins = Coins.ofNano(1)
    })
}
