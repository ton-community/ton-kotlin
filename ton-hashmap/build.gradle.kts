kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.tonBitstring)
                compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
                compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.3.2")
            }
        }
        val commonTest by getting
    }
}