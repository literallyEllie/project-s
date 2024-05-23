plugins {
    id("convention-http-client")
    id("convention-style")
    id("convention-test")
}

group = "com.elliegabel.s.http"
version = "0.0.1-SNAPSHOT"

dependencies {
    api(project(":commons"))
}