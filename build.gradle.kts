import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("io.github.gradle-nexus.publish-plugin") version "1.2.0"
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
    version = "0.2.16"

    apply(plugin = "kotlin-multiplatform")
    apply(plugin = "kotlinx-serialization")
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

    repositories {
        mavenCentral()
    }

    buildDir = File(rootDir, "build/${project.name}")

    tasks.withType<KotlinCompilationTask<*>> {
        compilerOptions.freeCompilerArgs.add("-opt-in=kotlin.io.encoding.ExperimentalEncodingApi")
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

        sourceSets.create("nativeMain").dependsOn(sourceSets["commonMain"])
        sourceSets.create("nativeTest").dependsOn(sourceSets["commonTest"])

        sourceSets.create("linuxMain").dependsOn(sourceSets["nativeMain"])
        sourceSets.create("linuxTest").dependsOn(sourceSets["nativeTest"])
        linuxX64 {
            compilations["main"].defaultSourceSet.dependsOn(sourceSets["linuxMain"])
            compilations["test"].defaultSourceSet.dependsOn(sourceSets["linuxTest"])
        }

        sourceSets.create("mingwMain").dependsOn(sourceSets["nativeMain"])
        sourceSets.create("mingwTest").dependsOn(sourceSets["nativeTest"])
        mingwX64 {
            compilations["main"].defaultSourceSet.dependsOn(sourceSets["mingwMain"])
            compilations["test"].defaultSourceSet.dependsOn(sourceSets["mingwTest"])
        }

        sourceSets.create("darwinMain").dependsOn(sourceSets["nativeMain"])
        sourceSets.create("darwinTest").dependsOn(sourceSets["nativeTest"])
        macosArm64 {
            compilations["main"].defaultSourceSet.dependsOn(sourceSets["darwinMain"])
            compilations["test"].defaultSourceSet.dependsOn(sourceSets["darwinTest"])
        }
        macosX64 {
            compilations["main"].defaultSourceSet.dependsOn(sourceSets["darwinMain"])
            compilations["test"].defaultSourceSet.dependsOn(sourceSets["darwinTest"])
        }
        ios {
            compilations["main"].defaultSourceSet.dependsOn(sourceSets["darwinMain"])
            compilations["test"].defaultSourceSet.dependsOn(sourceSets["darwinTest"])
        }

        sourceSets {
            val commonMain by getting
            val commonTest by getting {
                dependencies {
                    implementation(kotlin("test"))
                }
            }
            all {
                languageSettings {
                    optIn("kotlin.ExperimentalUnsignedTypes")
                    optIn("kotlin.contracts.ExperimentalContracts")
                    optIn("kotlin.io.encoding.ExperimentalEncodingApi")
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

fun Project.formatSourceSets() {
    kotlinExtension.sourceSets.all {
        val suffixIndex = name.indexOfLast { it.isUpperCase() }
        val targetName = name.substring(0, suffixIndex)
        val suffix = name.substring(suffixIndex).lowercase().takeIf { it != "main" }
        kotlin.srcDir("$targetName/${suffix ?: "src"}")
        resources.srcDir("$targetName/${suffix?.let { it + "Resources" } ?: "resources"}")
    }
}
