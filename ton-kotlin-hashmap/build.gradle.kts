kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.tonKotlinBitstring)
                api(projects.tonKotlinTlb)
                implementation(libs.serialization.json)
                implementation(libs.serialization.json.jvm)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(projects.tonKotlinBoc)
            }
        }
    }
}
