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
        findByName("jvmMain")?.dependencies {
            api(libs.ktor.network)
        }
        findByName("darwinMain")?.dependencies {
            api(libs.ktor.network)
        }
        findByName("linuxMain")?.dependencies {
            api(libs.ktor.network)
        }
        findByName("mingwMain")?.dependencies {
            api(libs.ktor.utils)
        }
        findByName("jvmTest")?.dependencies {
            implementation(libs.coroutines.jvm)
            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.utils)
            implementation("org.jetbrains.kotlinx:lincheck:2.17")
        }
    }
}
