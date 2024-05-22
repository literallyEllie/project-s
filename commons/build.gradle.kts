plugins {
    id("convention-java")
    id("convention-style")
    id("convention-test")
}

group = "com.elliegabel.s.commons"
version = "0.0.1-SNAPSHOT"

dependencies {
    api(libs.avaje.config)
    api(libs.gson)
    api(libs.annotations)
    implementation(libs.slf4j.api)
}

tasks.getByName<Jar>("jar") {
    archiveFileName.set("${project.name}.jar")
}