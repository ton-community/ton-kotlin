kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.tonBitstring)
                api(projects.tonBigint)
                api(projects.tonCrypto)
                implementation(libs.serialization.json)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
