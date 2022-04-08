kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.tonAdnl)
                implementation(projects.tonTlb)
                implementation(projects.tonBitstring)
                implementation(projects.tonCrypto)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}