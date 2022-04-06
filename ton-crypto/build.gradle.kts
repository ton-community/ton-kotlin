plugins {
    java
    id("org.jetbrains.kotlinx.benchmark")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.tonBitstring)
                implementation(projects.tonCommonBiginteger)
                api("com.github.andreypfau:curve25519-kotlin:1122884f40")
            }
        }
        jvmMain {
            dependencies {
                implementation("org.bouncycastle:bcprov-jdk15on:1.70")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-benchmark-runtime:0.4.2")
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