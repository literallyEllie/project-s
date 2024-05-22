import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.kotlin.dsl.the

plugins {
    id("convention-java")
}

val libs = the<LibrariesForLibs>()

dependencies {
    api(libs.avaje.inject)
    annotationProcessor(libs.avaje.inject.generator)
}
//
//val discoverModules = tasks.findByName("discoverModules")
//if (discoverModules != null) {
//    project.afterEvaluate { discoverModules.run }
//    discoverModules.doLast {  }
//    discoverModules.dependsOn()
//} else {
//    println("you forgot the io.avaje.inject gradle plugin!")
//}
