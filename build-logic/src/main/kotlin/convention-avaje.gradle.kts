import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.kotlin.dsl.the

plugins {
    id("convention-java")
    id("io.avaje.inject")
}

val libs = the<LibrariesForLibs>()

dependencies {
    api(libs.avaje.inject)
    annotationProcessor(libs.avaje.inject.generator)
}
