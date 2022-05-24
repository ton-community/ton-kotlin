rootProject.name = "ton-kotlin"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        kotlin("multiplatform") version "1.6.21"
        kotlin("plugin.serialization") version "1.6.21"
        id("org.jetbrains.kotlinx.benchmark") version "0.4.2"
    }
}

enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
    }
}

include(":ton-adnl")
include(":ton-api")
include(":ton-bigint")
include(":ton-bitstring")
include(":ton-block")
include(":ton-block-tlb")
include(":ton-cell")
include(":ton-crypto")
include(":ton-hashmap")
include(":ton-hashmap-tlb")
include(":ton-lite-api")
include(":ton-lite-client")
include(":ton-mnemonic")
include(":ton-tl")
include(":ton-tlb")
