kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.tonBitstring)
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
            }
        }
    }
}