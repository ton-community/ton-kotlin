plugins {
    id("buildsrc.convention.multiplatform")
}

dependencies {
    commonMainApi(projects.tonKotlinTl)
    commonMainApi(projects.tonKotlinCore)
}