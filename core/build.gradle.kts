plugins {
    id("buildsrc.convention.multiplatform")
    id("buildsrc.convention.layout")
    id("buildsrc.convention.publish")
}

dependencies {
    commonMainApi(projects.tonKotlinCrypto)
    commonMainApi(libs.kotlinx.io)
    commonMainApi(libs.coroutines.core)
    commonMainImplementation(libs.bignum)
    commonMainImplementation(libs.serialization.core)
}
