plugins {
    id("convention-avaje")
    id("convention-style")
    id("convention-test")
}

group = "com.elliegabel.s.player"
version = "0.0.1-SNAPSHOT"

dependencies {
    api(project(":commons"))
}
repositories {
    mavenCentral()
}