kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.tonAdnl)
                implementation(projects.tonTlb)
                implementation(projects.tonBitstring)
                implementation(projects.tonCrypto)
                implementation(projects.tonCell)
                implementation(projects.tonTlb)
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