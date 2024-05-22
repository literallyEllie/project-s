import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

val excludeClasses = listOf(
    "*.di.*"
)

val libs = the<LibrariesForLibs>()
val testImplementation = configurations.findByName("testImplementation")

dependencies {
    testImplementation?.let { testImplementation ->
        testImplementation(libs.test.junit)
        testImplementation(libs.test.mockk)
    }
}

tasks.withType<Test> {
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        showCauses = true
        showExceptions = true
        showStackTraces = true
        showStandardStreams = true
    }
}