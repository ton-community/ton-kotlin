plugins {
    id("buildsrc.convention.multiplatform")
}

dependencies {
    commonMainApi(projects.tonKotlinTonapiTl)
    commonMainApi(libs.datetime)
}