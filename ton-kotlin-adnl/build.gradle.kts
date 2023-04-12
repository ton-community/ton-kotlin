kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.tonKotlinApi)
                implementation(libs.serialization.json)
                implementation(projects.tonKotlinLogger)
                implementation(libs.atomicfu)
                implementation(libs.datetime)
                implementation(libs.coroutines.core)
                implementation(libs.ktor.utils)
                api("io.github.reactivecircus.cache4k:cache4k:0.9.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.coroutines.core)
                implementation(libs.coroutines.test)
                implementation(libs.coroutines.debug)
                implementation(libs.ktor.utils)
                implementation(projects.tonKotlinCrypto)
            }
        }
        val jvmMain by getting {
            dependencies {
                api(libs.ktor.network)
            }
        }
        val darwinMain by getting {
            dependencies {
                api(libs.ktor.network)
            }
        }
        val linuxMain by getting {
            dependencies {
                api(libs.ktor.network)
            }
        }
        val mingwMain by getting {
            dependencies {
                api(libs.ktor.utils)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(libs.coroutines.jvm)
                implementation(libs.ktor.client.cio)
                implementation(libs.ktor.utils)
            }
        }
    }
}
