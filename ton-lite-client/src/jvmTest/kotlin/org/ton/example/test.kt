package org.ton.example

import org.ton.api.adnl.AdnlPing
import org.ton.api.adnl.AdnlPong
import org.ton.api.adnl.message.AdnlMessage
import org.ton.crypto.hex
import org.ton.tl.TlCombinator

fun main() {
    val input =
        hex("c1183664ef573b6b3840b4eb8c45f89681da02e44077ce75470136890ad0f174296ffe55320261d585fb0b17473b715898051695c447e55d400334608ab32939520714bc513d6518ab8c33e216f562c079c4e0644a7be4ab0a96968c71bc95070ec2bf279b5fc690d6b54f11850ee1560dcc5097d95fc3599e0316afb08c5531055eaa5a7c3477ac25b10a322b47fba2384a3d026b99415a16a769acdb14c953e91d290819c25e6e7cdafe24a2e3f53ab2ea8add43cf3bf588fdc1efa3b818d7fd87a4424cd643b074d6b1146d136ec56cd70a8a00252e77568b889991c752e7")

    val decoded = AdnlTlCombinator.decodeBoxed(input)

    println(decoded)

}

private object AdnlTlCombinator : TlCombinator<Any>(
    AdnlMessage.constructors + AdnlPing + AdnlPong
)