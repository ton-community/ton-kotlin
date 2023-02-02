plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
    `maven-publish`
    signing
}

val isCI = System.getenv("CI") == "true"

//val githubVersion = System.getenv("GITHUB_REF")?.substring(11)
//if (isCI) {
//    checkNotNull(githubVersion) { "GITHUB_REF is not set" }
//    check(githubVersion.isNotEmpty()) { "GITHUB_REF is empty" }
//    check(githubVersion.matches(Regex("[0-9]+\\.[0-9]+\\.[0-9]+(-[a-zA-Z0-9]+)?"))) { "'$githubVersion' is not a valid version" }
//}

allprojects {
    group = "org.ton"
    version = "0.2.10"

    apply(plugin = "kotlin-multiplatform")
    apply(plugin = "kotlinx-serialization")
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

    repositories {
        mavenCentral()
    }

    kotlin {
        if (!isCI) {
            explicitApiWarning()
        }
        jvm {
            compilations.all {
                kotlinOptions {
                    jvmTarget = "1.8"
                }
            }
        }

        nativeTargets(NativeState.ALL) {
            common("darwin") {
                target("macosX64")
                target("macosArm64")
                target("iosX64")
                target("iosArm64")
                target("iosSimulatorArm64")
//                target("watchosArm64")
//                target("watchosX86")
//                target("watchosX64")
//                target("watchosSimulatorArm64")
//                target("tvosArm64")
//                target("tvosX64")
//                target("tvosSimulatorArm64")
            }
            common("mingw") {
                target("mingwX64")
            }
            common("linux") {
                target("linuxX64")
            }
        }

        sourceSets {
            val commonTest by getting {
                dependencies {
                    implementation(kotlin("test"))
                }
            }
        }
    }

    afterEvaluate {
        formatSourceSets()
    }

    val javadocJar by tasks.registering(Jar::class) {
        archiveClassifier.set("javadoc")
    }

    publishing {
        publications.withType<MavenPublication> {
            artifact(javadocJar.get())
            pom {
                name.set("ton-kotlin")
                description.set("Kotlin/Multiplatform SDK for The Open Network")
                url.set("https://github.com/andreypfau/ton-kotlin")
                licenses {
                    license {
                        name.set("The Apache Software License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
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
//        repositories {
//            maven {
//                name = "GitHubPackages"
//                url = uri("https://maven.pkg.github.com/andreypfau/ton-kotlin")
//                credentials {
//                    username = System.getenv("GITHUB_ACTOR")
//                    password = System.getenv("GITHUB_TOKEN")
//                }
//            }
//        }
    }

    signing {
        val secretKey = project.findProperty("signing.secretKey") as? String ?: System.getenv("SIGNING_SECRET_KEY")
        val password = project.findProperty("signing.password") as? String ?: System.getenv("SIGNING_PASSWORD")
        isRequired = secretKey != null && password != null
        useInMemoryPgpKeys(secretKey, password)
        sign(publishing.publications)
    }
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                subprojects {
                    api(this)
                }
            }
        }
    }
}

nexusPublishing {
    repositories {
        sonatype {
            username.set(project.findProperty("ossrhUsername") as? String ?: System.getenv("OSSRH_USERNAME"))
            password.set(project.findProperty("ossrhPassword") as? String ?: System.getenv("OSSRH_PASSWORD"))
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}
