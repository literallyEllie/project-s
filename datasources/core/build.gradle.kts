plugins {
    id("convention-java")
    id("convention-style")
    id("convention-test")
}

group = "com.elliegabel.s.datasource"
version = "0.0.1-SNAPSHOT"

dependencies {
    api(project(":commons"))
}