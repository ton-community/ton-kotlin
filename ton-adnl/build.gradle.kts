kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api("io.ktor:ktor-client-core:2.0.0-beta-1")
                api("io.ktor:ktor-client-cio:2.0.0-beta-1")
                api("io.ktor:ktor-network:2.0.0-beta-1")
                api(projects.tonTlb)
                api(projects.tonCrypto)
                api("org.jetbrains.kotlinx:atomicfu:0.17.1")
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