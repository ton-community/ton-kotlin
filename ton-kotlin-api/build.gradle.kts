kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.tonKotlinBigint)
                api(projects.tonKotlinTl)
                api(projects.tonKotlinBitstring)
                api(projects.tonKotlinTlb)
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
