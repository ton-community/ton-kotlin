kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.tonTlb)
                implementation(projects.tonTl)
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