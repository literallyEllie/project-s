plugins {
    `kotlin-dsl`
}

dependencies {
    // plugins have to be declared here.
    // base plugins
    implementation(libs.plugin.spotless)

    // phat jar
    implementation(libs.plugin.shadowjar)

    // avaje
    implementation(libs.plugin.avaje.inject)

    // minecraft
    implementation(libs.plugin.paperweight)
    implementation(libs.plugin.run.paper)
    implementation(libs.plugin.pluginyml.bukkit)

    // https://stackoverflow.com/questions/67795324/gradle7-version-catalog-how-to-use-it-with-buildsrc
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}