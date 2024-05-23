import org.gradle.kotlin.dsl.`java-library`

plugins {
    `java-library`
    id("com.github.johnrengelman.shadow")
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        mergeServiceFiles()
    }
}