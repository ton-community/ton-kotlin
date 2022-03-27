plugins {
    id("org.jetbrains.kotlinx.benchmark")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-benchmark-runtime:0.4.2")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
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
            iterationTime = 300
            iterationTimeUnit = "ms"
            advanced("jvmForks", 3)
        }
    }
}