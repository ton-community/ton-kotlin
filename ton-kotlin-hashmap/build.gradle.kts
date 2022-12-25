kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.tonKotlinBitstring)
                api(projects.tonKotlinTlb)
                implementation(libs.serialization.json)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(projects.tonKotlinBoc)
            }
        }
    }
}
