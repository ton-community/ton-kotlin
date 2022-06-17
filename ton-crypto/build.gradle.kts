plugins {
    java
    id("org.jetbrains.kotlinx.benchmark")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(libs.ktor.utils)
                api(libs.curve25519)
                compileOnly(libs.serialization.core)
            }
        }
        jvmMain {
            dependencies {
                implementation(libs.bouncycastle)
            }
        }
        commonTest {
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
