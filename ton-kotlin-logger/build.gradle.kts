kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.coroutines.core)
                implementation(libs.datetime)
                implementation(libs.atomicfu)
            }
        }
        val commonTest by getting
    }
}
