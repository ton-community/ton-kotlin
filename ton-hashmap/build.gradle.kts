kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.tonBitstring)
                api(projects.tonTlb)
                implementation(libs.serialization.json)
                implementation(libs.serialization.json.jvm)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(projects.tonBoc)
            }
        }
    }
}
