kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.tonBitstring)
                implementation(libs.serialization.json)
                implementation(libs.serialization.json.jvm)
            }
        }
        val commonTest by getting
    }
}
