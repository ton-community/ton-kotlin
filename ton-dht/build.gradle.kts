kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.tonAdnl)
                api(libs.coroutines.core)
                implementation(libs.atomicfu)
                implementation(libs.datetime)
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
