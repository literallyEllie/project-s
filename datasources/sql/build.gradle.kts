plugins {
    id("convention-avaje")
    id("convention-style")
    id("convention-test")
    id("io.avaje.inject") version "0.3"
}

group = "com.elliegabel.s.datasource"
version = "0.0.1-SNAPSHOT"

dependencies {
    api(project(":datasources:core"))

    api(libs.hikaricp)
    api(libs.postgres)
}