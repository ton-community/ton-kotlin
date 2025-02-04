import kotlinx.benchmark.gradle.JvmBenchmarkTarget
import org.jetbrains.kotlin.konan.target.HostManager
import org.jetbrains.kotlin.konan.target.KonanTarget

plugins {
    kotlin("multiplatform")
    alias(libs.plugins.kotlinx.benchmark.plugin)
}

kotlin {
    jvm()

    sourceSets {
        commonMain {
            dependencies {
                api(projects.tonKotlinCore)
                api(projects.tonKotlinDict)
                implementation(libs.kotlinx.benchmark.runtime)
            }
        }

        named("jvmMain") {
            dependsOn(commonMain.get())
        }
    }
}

val nativeBenchmarksEnabled: String = "true"

if (nativeBenchmarksEnabled.toBoolean()) {
    kotlin {
        // TODO: consider supporting non-host native targets.
        if (HostManager.host === KonanTarget.MACOS_X64) macosX64("native")
        if (HostManager.host === KonanTarget.MACOS_ARM64) macosArm64("native")
        if (HostManager.hostIsLinux) linuxX64("native")
        if (HostManager.hostIsMingw) mingwX64("native")

        sourceSets {
            named("nativeMain") {
                dependsOn(commonMain.get())
            }
        }
    }
}

benchmark {
    targets {
        register("jvm") {
            this as JvmBenchmarkTarget
            jmhVersion = libs.versions.jmh.get()
        }
        if (nativeBenchmarksEnabled.toBoolean()) {
            register("native")
        }
    }
}