import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.kotlin.dsl.the

plugins {
    id("convention-avaje")
}

val libs = the<LibrariesForLibs>()

dependencies {
    api(libs.javalin)
    api(libs.avaje.http.api)
    annotationProcessor(libs.avaje.http.javalin.generator)
}