rootProject.name = "ton-kotlin"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        kotlin("multiplatform") version "1.6.10"
        kotlin("plugin.serialization") version "1.6.0"
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

include(":ton-bitstring")
include(":ton-cell")
include(":ton-fift")
include(":ton-tlb")
include(":ton-types")
include(":ton-common-biginteger")
