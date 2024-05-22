buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

plugins {
    id("convention-test")
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}