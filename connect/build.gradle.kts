plugins {
    id("multiplatform")
    id("publish")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(libs.ktor.client.core)
                implementation(libs.sha2)
                implementation(libs.salsa20)
                implementation(libs.poly1305)
                implementation(libs.curve25519)
                implementation(libs.serialization.json)
            }
        }
        all {
            if (name.contains("mingw")) {
                dependencies {
                    implementation(libs.ktor.client.winhttp)
                }
            } else if (!name.contains("native") && !name.contains("common")) {
                dependencies {
                    implementation(libs.ktor.client.cio)
                }
            }
        }
    }
}
