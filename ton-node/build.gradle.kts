kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.tonAdnl)
                api(projects.tonDht)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
