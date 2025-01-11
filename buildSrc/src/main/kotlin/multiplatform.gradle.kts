package buildsrc.convention

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
//    explicitApiWarning()
    explicitApi()

    jvm()

    sourceSets {
        all {
            languageSettings {
                optIn("kotlin.ExperimentalStdlibApi")
                optIn("kotlin.contracts.ExperimentalContracts")
                optIn("kotlin.io.encoding.ExperimentalEncodingApi")
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }

    configureNativePlatforms()
    configureSourceSetsLayout()
}

fun KotlinMultiplatformExtension.configureNativePlatforms() {
    applyDefaultHierarchyTemplate()

    iosX64()
    iosArm64()
    iosSimulatorArm64()
    tvosX64()
    tvosArm64()
    tvosSimulatorArm64()
    watchosArm32()
    watchosArm64()
    watchosX64()
    watchosSimulatorArm64()

    macosX64()
    macosArm64()

    linuxArm64()
    linuxX64()

    mingwX64()

    sourceSets {
        val posixMain by creating {
            dependsOn(nativeMain.get())
        }

        appleMain.get().dependsOn(posixMain)
        linuxMain.get().dependsOn(posixMain)
    }
}

fun KotlinMultiplatformExtension.configureSourceSetsLayout() {
    sourceSets.configureEach {
        val name = name
        when {
            name.endsWith("Test") -> {
                val suffix = if (name.startsWith("common")) "" else "@${name.removeSuffix("Test")}"
                kotlin.setSrcDirs(listOf("test$suffix"))
                resources.setSrcDirs(listOf("testResources$suffix"))
            }
            name.endsWith("Main") -> {
                val suffix = if (name.startsWith("common")) "" else "@${name.removeSuffix("Main")}"
                kotlin.setSrcDirs(listOf("src$suffix"))
                resources.setSrcDirs(listOf("resources$suffix"))
            }
        }
    }
}
