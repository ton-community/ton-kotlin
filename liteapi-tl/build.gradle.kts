plugins {
    id("multiplatform")
    id("publish")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.tonKotlinTonapiTl)
                api(projects.tonKotlinBlockTlb) //TODO: remove dependency
            }
        }
    }
}
