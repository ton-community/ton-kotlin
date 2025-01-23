plugins {
    id("buildsrc.convention.multiplatform")
    id("buildsrc.convention.layout")
    id("buildsrc.convention.publish")
}

dependencies {
    commonMainApi(projects.tonKotlinTlb)
    commonMainImplementation(libs.serialization.core)
}
