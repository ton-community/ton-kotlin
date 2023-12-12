plugins {
    id("multiplatform")
    id("publish")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.tonKotlinHashmapTlb)
                api(projects.tonKotlinTlb)
                api(projects.tonKotlinTl)
                implementation(libs.serialization.core)
            }
        }
    }
}
