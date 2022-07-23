kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.tonAdnl)
                api(projects.tonTlb)
                api(projects.tonBitstring)
                api(projects.tonCrypto)
                api(projects.tonCell)
                api(projects.tonTlb)
                api(projects.tonLiteApi)
                api(projects.tonLogger)
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
