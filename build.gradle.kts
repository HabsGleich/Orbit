plugins {
    `java-library`
    `maven-publish`
}

group = "dev.habsgleich"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    api("org.jetbrains:annotations:26.0.2")

    implementation("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
    testImplementation("org.projectlombok:lombok:1.18.36")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.36")

    api("org.reflections:reflections:0.10.2")
    api("org.hibernate.orm:hibernate-core:6.6.13.Final")
    implementation("org.hibernate.orm:hibernate-hikaricp:6.6.13.Final")

    runtimeOnly("org.postgresql:postgresql:42.7.5")

    testImplementation("ch.qos.logback:logback-classic:1.2.11")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    repositories {
        maven {
            name = "Nexus"
            url = uri(
                if (!version.toString().endsWith("SNAPSHOT")) {
                    "https://repo.defever.de/repository/maven-releases/"
                } else {
                    "https://repo.defever.de/repository/maven-snapshots/"
                }
            )
            credentials {
                username = findProperty("nexus.user") as String?
                password = findProperty("nexus.password") as String?
            }
        }
    }
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            pom {
                name.set("Orbit")
                description.set("A simple ORM for Java")
                url.set("https://github.com/habsgleich/orbit")
            }
        }
    }
}