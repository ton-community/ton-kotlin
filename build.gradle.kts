plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.kotlinx.benchmark")
    `maven-publish`
    signing
}

allprojects {
    group = "org.ton"
    version = "0.0.1-SNAPSHOT"

    apply(plugin = "kotlin-multiplatform")
    apply(plugin = "kotlinx-serialization")
    apply(plugin = "maven-publish")

    repositories {
        mavenCentral()
        maven("https://jitpack.io")
    }

    kotlin {
        jvm {
            withJava()
            compilations.all {
                kotlinOptions.jvmTarget = "11"
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

publishing {
    publications {
        create<MavenPublication>("ton-kotlin") {
            pom {
                name.set("ton-kotlin")
                description.set("Pure Kotlin implementation of The Open Network")
                url.set("https://github.com/andreypfau/ton-kotlin")
                licenses {
                    license {
                        name.set("GNU General Public License v3.0")
                        url.set("https://www.gnu.org/licenses/gpl-3.0.html")
                    }
                }
                developers {
                    developer {
                        id.set("andreypfau")
                        name.set("Andrey Pfau")
                        email.set("andreypfau@ton.org")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/andreypfau/ton-kotlin.git")
                    developerConnection.set("scm:git:ssh://github.com/andreypfau/ton-kotlin.git")
                    url.set("https://github.com/andreypfau/ton-kotlin")
                }
            }
        }
    }

    repositories {
        maven {
            val releasesUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotsUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            url = if (version.toString().contains("SNAPSHOT")) snapshotsUrl else releasesUrl
            credentials {
                username = project.properties["ossrh_username"].toString()
                password = project.properties["ossrh_password"].toString()
            }
        }
    }
}

signing {
    sign(publishing.publications)
}