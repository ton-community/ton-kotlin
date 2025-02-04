plugins {
    id("buildsrc.convention.multiplatform")
    id("buildsrc.convention.layout")
    id("buildsrc.convention.publish")
}

dependencies {
    commonMainApi(projects.tonKotlinHashmapTlb)
    commonMainImplementation(libs.serialization.core)
}
