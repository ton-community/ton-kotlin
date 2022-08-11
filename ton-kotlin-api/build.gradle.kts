kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.tonKotlinBigint)
                api(projects.tonKotlinTl)
                api(projects.tonKotlinTlb)
                implementation(libs.serialization.json)
                api(libs.datetime)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
