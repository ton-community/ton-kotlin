kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.tonCell)
                api(projects.tonTlb)
                api(projects.tonBlock)
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
