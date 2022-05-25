kotlin {
    sourceSets {
        commonMain {
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
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
