kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.tonAdnl)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}