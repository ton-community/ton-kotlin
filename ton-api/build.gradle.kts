kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.tonBigint)
                api(projects.tonTl)
                api(projects.tonTlb)
                api(projects.tonCrypto)
                implementation(libs.serialization.json)
                api(libs.datetime)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}