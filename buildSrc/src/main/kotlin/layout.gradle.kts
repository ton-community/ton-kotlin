package buildsrc.convention

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    kotlin("multiplatform")
}

kotlin {
    configureSourceSetsLayout()
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
