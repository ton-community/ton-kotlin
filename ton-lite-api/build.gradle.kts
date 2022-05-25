kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.tonTl)
                api(projects.tonTlb)
                api(projects.tonBlock)
                api(projects.tonBlockTlb)
                api(projects.tonBitstring)
                api(projects.tonCrypto)
                api(projects.tonCell)
                api(projects.tonApi)
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
