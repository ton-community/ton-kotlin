plugins {
    java
    id("org.jetbrains.kotlinx.benchmark")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.ktor.utils)
                implementation(libs.curve25519)
                implementation(libs.kotlinio.base64)
                compileOnly(libs.serialization.core)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.bouncycastle)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.benchmark.runtime)
            }
        }
    }
}

benchmark {
    configurations {
        targets {
            // This one matches compilation base name, e.g. 'jvm', 'jvmTest', etc
            register("jvm")
        }

        val main by getting {
            iterations = 5 // number of iterations
            iterationTime = 1000
            iterationTimeUnit = "ms"
        }
    }
}
