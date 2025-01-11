plugins {
    id("buildsrc.convention.multiplatform")
}

dependencies {
    commonMainApi(projects.tonKotlinTlb)
    commonMainImplementation(libs.serialization.core)
}
