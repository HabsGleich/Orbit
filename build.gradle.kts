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

    implementation("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
    testImplementation("org.projectlombok:lombok:1.18.38")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.38")

    api("org.reflections:reflections:0.10.2")
    api("org.hibernate.orm:hibernate-core:6.6.13.Final")
    implementation("org.hibernate.orm:hibernate-hikaricp:6.6.13.Final")

    runtimeOnly("org.postgresql:postgresql:42.7.7")

    testImplementation("ch.qos.logback:logback-classic:1.2.11")
    testImplementation(platform("org.junit:junit-bom:5.13.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    repositories {
        maven {
            name = "Repository"
            url = uri(
                if (!version.toString().endsWith("SNAPSHOT")) {
                    "https://maven.defever.de/releases"
                } else {
                    "https://maven.defever.de/snapshots"
                }
            )
            credentials {
                username = System.getenv("REPOSITORY_USERNAME")
                password = System.getenv("REPOSITORY_PASSWORD")
            }
        }
    }
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            pom {
                name.set("Orbit")
                description.set("A simple ORM for Java based on Hibernate")
                url.set("https://github.com/habsgleich/orbit")

                developers {
                    developer {
                        id.set("habsgleich")
                        name.set("HabsGleich")
                        email.set("nick@defever.de")
                    }
                }

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/license/mit/")
                    }
                }
            }
        }
    }
}