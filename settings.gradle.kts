rootProject.name = "ton-kotlin"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        kotlin("multiplatform") version "1.6.20"
        kotlin("plugin.serialization") version "1.6.0"
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

include(":ton-crypto")
include(":ton-bitstring")
include(":ton-cell")
include(":ton-fift")
include(":ton-adnl")
include(":ton-lite-client")
include(":ton-tlb")
include(":ton-types")
include(":ton-hashmap")
include(":ton-hashmap-tlb")
include(":ton-block")
include(":ton-block-tlb")
include(":ton-primitives")
include(":ton-common-biginteger")
