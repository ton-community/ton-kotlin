plugins {
    id("multiplatform")
    id("publish")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.tonKotlinTvm)
                implementation(libs.atomicfu)
                implementation(kotlin("reflect"))
            }
        }
    }
}
