plugins {
    id("convention-http-client")
    id("convention-style")
    id("convention-test")
}

group = "com.elliegabel.s.player"
version = "0.0.1-SNAPSHOT"

dependencies {
    api(project(":http:client"))
    api(project(":services:player-service:core"))
}