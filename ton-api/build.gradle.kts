kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.tonBigint)
                implementation(projects.tonTl)
                implementation(projects.tonTlb)
                implementation(projects.tonCrypto)
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