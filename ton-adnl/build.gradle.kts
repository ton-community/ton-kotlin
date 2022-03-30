kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation("io.ktor:ktor-client-core:2.0.0-beta-1")
                implementation("io.ktor:ktor-client-cio:2.0.0-beta-1")
                implementation("io.ktor:ktor-network:2.0.0-beta-1")
                implementation(projects.tonTlb)
                implementation(projects.tonCrypto)
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