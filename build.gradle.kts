plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.kotlinx.benchmark")
    `maven-publish`
}

allprojects {
    group = "com.github.andreypfau"
    version = "1.0-SNAPSHOT"

    apply(plugin = "kotlin-multiplatform")
    apply(plugin = "kotlinx-serialization")
    apply(plugin = "maven-publish")

    repositories {
        mavenCentral()
    }

    kotlin {
        jvm {
            withJava()
            compilations.all {
                kotlinOptions.jvmTarget = "17"
            }
            testRuns["test"].executionTask.configure {
                useJUnitPlatform()
            }
        }
//        linuxX64()
//        macosX64()
//        js {
//            useCommonJs()
//            browser()
//        }
        sourceSets {
            val commonMain by getting {
                dependencies {
                    subprojects {
                        api(this)
                    }
                }
            }
            val commonTest by getting {
                dependencies {
                    implementation(kotlin("test"))
                }
            }
        }
    }
}
