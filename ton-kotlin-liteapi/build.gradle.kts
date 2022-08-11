kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.tonKotlinTl)
                api(projects.tonKotlinTlb)
                api(projects.tonKotlinBlock)
                api(projects.tonKotlinCrypto)
                api(projects.tonKotlinCell)
                api(projects.tonKotlinBoc)
                api(projects.tonKotlinApi)
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
