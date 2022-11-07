kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.tonKotlinCrypto)
                api(projects.tonKotlinBitstring)
                implementation(libs.ktor.utils)
                implementation(libs.serialization.json)
                implementation(kotlin("reflect"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
