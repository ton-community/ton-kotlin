import java.util.*

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.kotlinx.benchmark")
    `maven-publish`
    signing
}

val localPropsFile = project.rootProject.file("local.properties")
if (localPropsFile.exists()) {
    val p = Properties()
    localPropsFile.inputStream().use { p.load(it) }
    p.forEach { name, value -> ext.set(name.toString(), value) }
}

allprojects {
    group = "org.ton"
    version = "0.0.3"

    apply(plugin = "kotlin-multiplatform")
    apply(plugin = "kotlinx-serialization")
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

    repositories {
        mavenCentral()
        maven("https://jitpack.io")
    }

    kotlin {
        jvm {
            withJava()
            compilations.all {
                kotlinOptions.jvmTarget = "1.8"
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
                    implementation("io.mockk:mockk:1.12.4")
                    implementation(kotlin("test"))
                }
            }
            val jvmMain by getting {

            }
            val jvmTest by getting {
                dependencies {
                    implementation(kotlin("test"))
                    implementation("junit:junit:4.13.1")
                }
            }
        }
    }

    val javadocJar by tasks.registering(Jar::class) {
        archiveClassifier.set("javadoc")
    }

    publishing {
        repositories {
            maven {
                val releasesUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                val snapshotsUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                url = if (version.toString().contains("SNAPSHOT")) snapshotsUrl else releasesUrl
                credentials {
                    username = project.properties["ossrhUsername"].toString()
                    password = project.properties["ossrhPassword"].toString()
                }
            }
        }

        publications.withType<MavenPublication> {
            artifact(javadocJar.get())
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

    val signingKeyId = project.properties["signing.keyId"]?.toString()
    val signingSecretKey = project.properties["signing.secretKey"]?.toString()
    val signingPassword = project.properties["signing.password"]?.toString()

    if (signingKeyId != null && signingSecretKey != null && signingPassword != null) {
        signing {
            sign(publishing.publications)
            useInMemoryPgpKeys(signingKeyId, signingSecretKey, signingPassword)
        }
    }
}
