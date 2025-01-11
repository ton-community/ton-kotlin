rootProject.name = "ton-kotlin"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

submodule("core")
submodule("crypto")
submodule("tl")
submodule("tlb")
submodule("hashmap-tlb")
submodule("block-tlb")
submodule("tonapi-tl")
submodule("liteapi-tl")
submodule("adnl")
submodule("liteclient")
submodule("contract")

fun submodule(name: String) {
    include(":ton-kotlin-$name")
    project(":ton-kotlin-$name").projectDir = file(name)
}
