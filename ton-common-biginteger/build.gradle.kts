kotlin {
    macosX64() {
        binaries {
            executable {
                entryPoint = "ton.test.main"
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
            }
        }
    }
}