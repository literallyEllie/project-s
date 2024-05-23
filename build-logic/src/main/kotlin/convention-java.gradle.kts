import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    `java-library`
    `maven-publish`
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    sourceCompatibility = "17"
    targetCompatibility = "17"

}

val libs = the<LibrariesForLibs>()
val implementation by configurations

publishing {
    publishing {
        publications.create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}