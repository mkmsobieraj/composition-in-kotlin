plugins {
    kotlin("jvm") version "1.8.21"
}

group = "org.example"
version = "1.0-SNAPSHOT"

subprojects {
    apply(plugin = "kotlin")

    repositories {
        mavenCentral()
    }

    dependencies {
        testImplementation(kotlin("test"))
    }
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}