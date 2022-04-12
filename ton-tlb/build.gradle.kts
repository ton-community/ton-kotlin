kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.tonCell)
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
            }
        }
    }

    js {
        browser()
    }
}