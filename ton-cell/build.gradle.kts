kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.tonBitstring)
                implementation("io.ktor:ktor-utils:2.0.0-beta-1")
                compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
            }
        }
    }

    js {
        browser()
        binaries.executable()
    }
}