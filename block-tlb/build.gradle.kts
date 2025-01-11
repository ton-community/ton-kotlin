plugins {
    id("buildsrc.convention.multiplatform")
}

dependencies {
    commonMainApi(projects.tonKotlinTlb)
    commonMainApi(projects.tonKotlinHashmapTlb)
    commonMainImplementation(libs.serialization.core)
}
