kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.tonKotlinAdnl)
                api(projects.tonKotlinLogger)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation ("io.ktor:ktor-server-cio:2.2.4")
            }
        }
    }
}
