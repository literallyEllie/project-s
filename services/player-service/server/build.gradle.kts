plugins {
    id("convention-http-server")
    id("convention-style")
    id("convention-test")
    id("convention-app")
}

group = "com.elliegabel.s.player"
version = "0.0.1-SNAPSHOT"

dependencies {
    api(project(":http:server"))
    api(project(":datasources:sql"))
    api(project(":services:player-service:client"))
}

tasks {
    shadowJar {
        archiveFileName = "player-service.jar"
    }

    jar {
        manifest {
            attributes["Main-Class"] = "com.elliegabel.s.player.PlayerServiceApp"
        }
    }
}
