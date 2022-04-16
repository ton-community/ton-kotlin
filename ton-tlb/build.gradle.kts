kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.tonCell)
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
            }
        }
    }

    js {
        browser()
    }
}