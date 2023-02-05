package org.ton.tonlib

import kotlinx.serialization.json.Json
import org.ton.api.liteclient.config.LiteClientConfigGlobal
import org.ton.bitstring.Bits256

internal open class MasterConfig {
    private val byRootHash = mutableMapOf<Bits256, LiteClientConfigGlobal>()
    private val byName = mutableMapOf<String, LiteClientConfigGlobal>()

    fun addConfig(name: String, json: String) {
        addConfig(name, parse(json))
    }

    fun addConfig(name: String, config: LiteClientConfigGlobal) {
        byName[name] = config
        byRootHash[config.validator.zeroState.rootHash] = config
    }

    operator fun get(name: String): LiteClientConfigGlobal? {
        return byName[name]
    }

    operator fun get(rootHash: Bits256): LiteClientConfigGlobal? {
        return byRootHash[rootHash]
    }

    companion object : MasterConfig() {
        private val JSON = Json {
            ignoreUnknownKeys = true
        }

        fun parse(json: String): LiteClientConfigGlobal =
            JSON.decodeFromString(LiteClientConfigGlobal.serializer(), json)

        init {
            addConfig(
                "mainnet", """
{
  "@type": "config.global",
  "dht": {
    "@type": "dht.config.global",
    "k": 6,
    "a": 3,
    "static_nodes": {
      "@type": "dht.nodes",
      "nodes": []
    }
  },
  "liteservers": [],
  "validator": {
    "@type": "validator.config.global",
    "zero_state": {
      "workchain": -1,
      "shard": -9223372036854775808,
      "seqno": 0,
      "root_hash": "F6OpKZKqvqeFp6CQmFomXNMfMj2EnaUSOXN+Mh+wVWk=",
      "file_hash": "XplPz01CXAps5qeSWUtxcyBfdAo5zVb1N979KLSKD24="
    },
    "init_block": {
      "root_hash": "irEt9whDfgaYwD+8AzBlYzrMZHhrkhSVp3PU1s4DOz4=",
      "seqno": 10171687,
      "file_hash": "lay/bUKUUFDJXU9S6gx9GACQFl+uK+zX8SqHWS9oLZc=",
      "workchain": -1,
      "shard": -9223372036854775808
    },
    "hardforks": [
      {
        "file_hash": "t/9VBPODF7Zdh4nsnA49dprO69nQNMqYL+zk5bCjV/8=",
        "seqno": 8536841,
        "root_hash": "08Kpc9XxrMKC6BF/FeNHPS3MEL1/Vi/fQU/C9ELUrkc=",
        "workchain": -1,
        "shard": -9223372036854775808
      }
    ]
  }
}
            """.trimIndent()
            )
        }
    }
}
