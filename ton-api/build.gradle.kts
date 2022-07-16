kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.tonBigint)
                api(projects.tonTl)
                api(projects.tonTlb)
                api(projects.tonCrypto)
                implementation(libs.serialization.json)
                api(libs.datetime)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}