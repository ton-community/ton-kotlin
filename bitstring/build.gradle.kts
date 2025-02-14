plugins {
    id("multiplatform")
    id("publish")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.tonKotlinCrypto)
                implementation(libs.serialization.core)
                implementation(libs.kotlinx.io)
            }
        }
    }
}
