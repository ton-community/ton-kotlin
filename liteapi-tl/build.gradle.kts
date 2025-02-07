plugins {
    id("buildsrc.convention.multiplatform")
    id("buildsrc.convention.layout")
    id("buildsrc.convention.publish")
}

dependencies {
    commonMainApi(projects.tonKotlinTl)
    commonMainApi(projects.tonKotlinTonapiTl)
    commonMainApi(projects.tonKotlinBlockTlb) // TODO: remove, used in LiteServerRunSmcMethod
}

