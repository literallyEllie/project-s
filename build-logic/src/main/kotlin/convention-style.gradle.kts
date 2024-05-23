import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("com.diffplug.spotless")
}

val libs = the<LibrariesForLibs>()

spotless {
    format("misc") {
        target("*.gradle", "*.gitignore")
        indentWithSpaces()
        trimTrailingWhitespace()
        endWithNewline()
    }
    json {
        target("*.json")
        simple()
    }
    yaml {
        target("*.yml", "*.yaml")
        jackson()
    }
}