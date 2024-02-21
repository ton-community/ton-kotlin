plugins {
    id("multiplatform")
    id("publish")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.tonKotlinCrypto)
                api(projects.tonKotlinBitstring)
                api(libs.ktor.utils)
                api(libs.serialization.json)
                api(libs.kotlinx.io)
            }
        }
    }
}
