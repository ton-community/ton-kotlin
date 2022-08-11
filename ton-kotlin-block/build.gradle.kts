import kotlinx.benchmark.gradle.benchmark
import org.jetbrains.kotlin.allopen.gradle.AllOpenExtension

plugins {
    id("org.jetbrains.kotlinx.benchmark")
    kotlin("plugin.allopen") version "1.7.0"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.tonKotlinBoc)
                api(projects.tonKotlinHashmap)
                api(projects.tonKotlinTlb)
                implementation(libs.serialization.json)
            }
        }
        val jvmTest by getting {
            dependencies {
                api(libs.benchmark.runtime)
            }
        }
    }
}

configure<AllOpenExtension> {
    annotation("org.openjdk.jmh.annotations.State")
}

benchmark {
    configurations {
        targets {
            register("jvm")
        }

        val main by getting {
            iterations = 5 // number of iterations
            iterationTime = 5000
            iterationTimeUnit = "ms"
        }
    }
}

