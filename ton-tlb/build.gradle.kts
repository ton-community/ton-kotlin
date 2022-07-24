kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.tonCell)
                implementation(libs.atomicfu)
                implementation(kotlin("reflect"))
            }
        }
    }
}