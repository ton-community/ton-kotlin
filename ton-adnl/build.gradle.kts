kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.tonTlb)
                api(projects.tonCrypto)
                api(libs.ktor.client.core)
                api(libs.ktor.client.cio)
                api(libs.ktor.network)
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