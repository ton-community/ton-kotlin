plugins {
    id("multiplatform")
    id("publish")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.tonKotlinBigint)
                api(projects.tonKotlinTl)
                api(projects.tonKotlinBitstring)
                api(projects.tonKotlinTlb)
                api(libs.datetime)
            }
        }
    }
}
