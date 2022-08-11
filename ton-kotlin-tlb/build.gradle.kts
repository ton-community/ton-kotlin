kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.tonKotlinCell)
                implementation(libs.atomicfu)
                implementation(kotlin("reflect"))
            }
        }
    }
}
