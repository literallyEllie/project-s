plugins {
    id("convention-http-server")
    id("convention-style")
    id("convention-test")
}

group = "com.elliegabel.s.http"
version = "0.0.1-SNAPSHOT"

dependencies {
    api(project(":commons"))
    api(project(":http:client"))
    implementation(libs.slf4j.simple)
}