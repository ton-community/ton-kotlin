kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.tonCell)
                api(projects.tonTlb)
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
