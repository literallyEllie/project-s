plugins {
    id("convention-avaje")
    id("convention-style")
    id("convention-test")
}

group = "com.elliegabel.s.datasource"
version = "0.0.1-SNAPSHOT"

dependencies {
    api(project(":datasources:core"))

    api(libs.mongodb.driver)
}