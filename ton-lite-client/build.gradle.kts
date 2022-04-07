kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.tonAdnl)
                implementation(projects.tonTlb)
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