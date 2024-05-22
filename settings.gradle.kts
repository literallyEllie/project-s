plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "project-s"

includeBuild("build-logic")

include(":commons")

include(":datasources:core")
include(":datasources:sql")
include(":datasources:mongodb")

include("http:server")
include("http:client")

include("services:player-service:core")
include("services:player-service:client")
include("services:player-service:server")
