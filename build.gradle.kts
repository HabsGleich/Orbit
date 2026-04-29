plugins {
    `java-library`
    `maven-publish`
    signing
    alias(libs.plugins.nexusPublish)
}

group = "dev.habsgleich"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    withSourcesJar()
    withJavadocJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    api(libs.jetbrainsAnnotations)

    implementation(libs.lombok)
    annotationProcessor(libs.lombok)

    api(libs.reflections)
    api(libs.bundles.hibernate)

    runtimeOnly(libs.postgresql)

    testImplementation(libs.lombok)
    testAnnotationProcessor(libs.lombok)
    testImplementation(libs.logbackClassic)
    testImplementation(libs.bundles.junit)
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = "dev.habsgleich"
            artifactId = "orbit"
            version = project.version.toString()

            from(components["java"])
            pom {
                name.set("Orbit")
                description.set("A Java Hibernate ORM Data Mapper with a focus on modularity and ease of use.")
                url.set("https://github.com/HabsGleich/Orbit")

                developers {
                    developer {
                        id.set("HabsGleich")
                        name.set("Nick Defever")
                        email.set("nick@defever.de")
                    }
                }

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/license/mit/")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/HabsGleich/Orbit.git")
                    developerConnection.set("scm:git:ssh://github.com:HabsGleich/Orbit.git")
                    url.set("https://github.com/HabsGleich/Orbit")
                }
            }
        }
    }
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))

            username.set(findProperty("ossrhUsername")?.toString() ?: System.getenv("OSSRH_USERNAME"))
            password.set(findProperty("ossrhPassword")?.toString() ?: System.getenv("OSSRH_PASSWORD"))
        }
    }
    useStaging.set(project.version.toString().endsWith("SNAPSHOT").not())
}

signing {
    setRequired {
        gradle.taskGraph.hasTask("publish") && !version.toString().endsWith("-SNAPSHOT")
    }
    sign(publishing.publications["mavenJava"])
}

tasks.withType<Javadoc> {
    val opts = options as StandardJavadocDocletOptions
    opts.encoding = "UTF-8"
    opts.addStringOption("Xdoclint:none", "-quiet")
    opts.addStringOption("encoding", "UTF-8")
    opts.addStringOption("charSet", "UTF-8")
}