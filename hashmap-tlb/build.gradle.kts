plugins {
    id("multiplatform")
    id("publish")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.tonKotlinBitstring)
                api(projects.tonKotlinTlb)
                implementation(libs.serialization.json)
            }
        }
    }
}
