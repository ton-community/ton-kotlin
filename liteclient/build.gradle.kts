plugins {
    id("buildsrc.convention.multiplatform")
}

dependencies {
    commonMainApi(projects.tonKotlinAdnl)
    commonMainApi(projects.tonKotlinLiteapiTl)
    commonMainApi(projects.tonKotlinBlockTlb)
    commonMainApi(libs.datetime)
    commonMainImplementation(libs.atomicfu)
}
