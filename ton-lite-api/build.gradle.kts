kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.tonTl)
                api(projects.tonTlb)
                api(projects.tonBlock)
                api(projects.tonBlockTlb)
                api(projects.tonCrypto)
                api(projects.tonCell)
                api(projects.tonBoc)
                api(projects.tonApi)
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
