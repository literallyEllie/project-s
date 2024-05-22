plugins {
    id("convention-http-server")
    id("convention-style")
    id("convention-test")
    id("io.avaje.inject") version "0.3"
}

group = "com.elliegabel.s.player"
version = "0.0.1-SNAPSHOT"

dependencies {
    api(project(":http:server"))
    api(project(":datasources:sql"))
    api(project(":services:player-service:client"))
}