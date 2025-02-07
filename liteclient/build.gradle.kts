plugins {
    id("buildsrc.convention.multiplatform")
    id("buildsrc.convention.layout")
    id("buildsrc.convention.publish")
}

dependencies {
    commonMainApi(projects.tonKotlinAdnl)
    commonMainApi(projects.tonKotlinLiteapiTl)
    commonMainApi(projects.tonKotlinBlockTlb)
    commonMainApi(projects.tonKotlinContract)
    commonMainApi(libs.datetime)
    commonMainImplementation(libs.atomicfu)
}
