kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.2")
            }
        }
    }

    js {
        browser()
        binaries.executable()
    }
}