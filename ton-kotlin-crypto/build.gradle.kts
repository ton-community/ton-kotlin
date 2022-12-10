kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.ktor.utils)
                implementation(libs.curve25519)
                implementation(libs.serialization.core)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.bouncycastle)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
