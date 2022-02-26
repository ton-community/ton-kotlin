kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("reflect"))
                implementation(projects.tonCommonBiginteger)
            }
        }
        val commonTest by getting
    }
}