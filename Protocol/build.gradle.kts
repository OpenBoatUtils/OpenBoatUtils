plugins {
    id("java")
    `maven-publish`
}

group = "io.github.openboatutils"
version = "0.0.7"

repositories {
    mavenCentral()
}

dependencies {}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(21)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}