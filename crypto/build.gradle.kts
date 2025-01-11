plugins {
    id("buildsrc.convention.multiplatform")
}

dependencies {
    commonMainApi(libs.sha2)
    commonMainApi(libs.aes)
    commonMainApi(libs.crc32)
    commonMainApi(libs.pbkdf2)
    commonMainApi(libs.hmac)
    commonMainImplementation(libs.curve25519)
}
