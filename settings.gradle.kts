rootProject.name = "ton-kotlin"

pluginManagement {
    repositories {
        maven(url = "https://maven.pkg.jetbrains.space/kotlin/p/kotlinx/maven")
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        kotlin("multiplatform") version "1.8.0"
        kotlin("plugin.serialization") version "1.9.10"
    }
}

System.setProperty("idea.active", "false")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
    }
}

include(":ton-kotlin-adnl")
include(":ton-kotlin-api")
include(":ton-kotlin-bigint")
include(":ton-kotlin-bitstring")
include(":ton-kotlin-block")
include(":ton-kotlin-boc")
include(":ton-kotlin-cell")
include(":ton-kotlin-crypto")
//include(":ton-kotlin-dht")
include(":ton-kotlin-hashmap")
include(":ton-kotlin-liteapi")
include(":ton-kotlin-liteclient")
include(":ton-kotlin-logger")
include(":ton-kotlin-mnemonic")
include(":ton-kotlin-rldp")
//include(":ton-kotlin-experimental")
include(":ton-kotlin-contract")
include(":ton-kotlin-tl")
include(":ton-kotlin-tlb")
include(":ton-kotlin-fift")
