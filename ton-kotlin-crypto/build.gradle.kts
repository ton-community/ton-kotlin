plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets {
        val commonMain  by getting  {
            dependencies {
                api(libs.ktor.utils)
                implementation(libs.curve25519)
                implementation(libs.serialization.core)
            }
        }
        val commonTest by getting  {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.bouncycastle)
            }
        }
        val nativeMain by getting {
            dependencies {
                api(libs.serialization.core)
            }
        }
    }
}
