plugins {
    `kotlin-dsl`
}

//kotlinDslPluginOptions {
//    jvmTarget.set("17")
//}

dependencies {
    implementation(libs.plugin.detekt)
    implementation(libs.plugin.spotless)

    // https://stackoverflow.com/questions/67795324/gradle7-version-catalog-how-to-use-it-with-buildsrc
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}