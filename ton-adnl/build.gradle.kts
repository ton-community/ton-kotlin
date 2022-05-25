kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.tonTl)
                api(projects.tonTlb)
                api(projects.tonCrypto)
                api(projects.tonApi)
                api(libs.ktor.client.core)
                api(libs.ktor.client.cio)
                api(libs.ktor.network)
                implementation(libs.serialization.json)
                implementation(projects.tonLogger)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
