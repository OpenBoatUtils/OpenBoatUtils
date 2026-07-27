plugins {
    id("java")
    `maven-publish`
}

group = "io.github.openboatutils"
version = "0.0.6"

repositories {
    mavenCentral()
}

dependencies {}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}