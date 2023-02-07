kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.tonKotlinBitstring)
                api(projects.tonKotlinBigint)
                api(projects.tonKotlinTlb)
                api(projects.tonKotlinBlock)
                api(projects.tonKotlinLogger)
                implementation(libs.serialization.json)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
