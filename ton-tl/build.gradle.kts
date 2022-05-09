kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.tonCrypto)
                implementation(libs.ktor.utils)
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