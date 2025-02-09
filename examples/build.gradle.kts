import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()
    configureSourceSetsLayout()

    sourceSets {
        commonMain {
            dependencies {
                api(projects.tonKotlinLiteclient)
                api(projects.tonKotlinContract)
            }
        }
    }
}

fun KotlinMultiplatformExtension.configureSourceSetsLayout() {
    sourceSets {
        all {
            if (name.endsWith("Main")) {
                val suffix = if (name.startsWith("common")) "" else "@${name.removeSuffix("Main")}"
                kotlin.srcDir("src$suffix")
                resources.srcDir("resources$suffix")
            }
            if (name.endsWith("Test")) {
                val suffix = if (name.startsWith("common")) "" else "@${name.removeSuffix("Test")}"
                kotlin.srcDir("test$suffix")
                resources.srcDir("testResources$suffix")
            }
        }
    }
}
