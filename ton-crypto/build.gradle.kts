plugins {
    java
    id("org.jetbrains.kotlinx.benchmark")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api("io.ktor:ktor-utils:2.0.0-beta-1")
            }
        }
        jvmMain {
            dependencies {
                implementation("com.github.andreypfau:curve25519-kotlin:f008dbc2c0")
                implementation("org.bouncycastle:bcprov-jdk15on:1.70")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-benchmark-runtime:0.4.2")
            }
        }
        js {
            browser()
            binaries.executable()
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