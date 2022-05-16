kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.tonCrypto)
                implementation(libs.ktor.utils)
                implementation(libs.serialization.json)
                implementation(kotlin("reflect"))
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}