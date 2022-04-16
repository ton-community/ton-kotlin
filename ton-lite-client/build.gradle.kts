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
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}