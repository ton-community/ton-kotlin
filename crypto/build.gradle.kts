plugins {
    id("multiplatform")
    id("publish")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(libs.ktor.utils)
//                api(libs.crypto)
                implementation(libs.curve25519)
                implementation(libs.serialization.core)
            }
        }
    }
}
