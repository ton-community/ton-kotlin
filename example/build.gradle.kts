plugins {
    id("buildsrc.convention.layout")
}

kotlin {
    jvm()
}

dependencies {
    commonMainApi(projects.tonKotlinLiteclient)
}
