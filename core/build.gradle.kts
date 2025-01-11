plugins {
    id("buildsrc.convention.multiplatform")
}

dependencies {
    commonMainApi(projects.tonKotlinCrypto)
    commonMainApi(libs.kotlinx.io)
    commonMainApi(libs.coroutines.core)
    commonMainImplementation(libs.bignum)
    commonMainImplementation(libs.serialization.core)
}
