plugins {
    alias(libs.plugins.kotlin)
    `maven-publish`
    signing
}

repositories {
    mavenCentral()
    google()
}

kotlin {
    explicitApi()
    jvmToolchain(17)
}

dependencies {
    testImplementation(libs.test.junit)
    testImplementation(libs.test.strikt)
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.ioki.result"
            artifactId = "result"
            version = "0.0.1-SNAPSHOT"

            pom {
                name.set("Result")
                description.set("Result library")
                url.set("https://github.com/ioki-mobility/Result")
                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                organization {
                    name.set("ioki")
                    url.set("https://ioki.com")
                }
                developers {
                    developer {
                        name.set("Stefan 'StefMa' M.")
                        email.set("StefMaDev@outlook.com")
                        url.set("https://StefMa.guru")
                        organization.set("ioki")
                        organizationUrl.set("https://ioki.com")
                    }
                }
                scm {
                    url.set("https://github.com/ioki-mobility/Result")
                    connection.set("scm:git:git://github.com/ioki-mobility/Result.git")
                    developerConnection.set("scm:git:ssh://git@github.com:ioki-mobility/Result.git")
                }
            }
            afterEvaluate {
//                from(components["release"])
            }
        }
    }
    repositories {
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/") {
            name = "SonatypeSnapshot"
            credentials {
                username = System.getenv("SONATYPE_USER")
                password = System.getenv("SONATYPE_PASSWORD")
            }
        }
        maven("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/") {
            name = "SonatypeStaging"
            credentials {
                username = System.getenv("SONATYPE_USER")
                password = System.getenv("SONATYPE_PASSWORD")
            }
        }
    }
}

//signing {
//    val signingKey = System.getenv("GPG_SIGNING_KEY")
//    val signingPassword = System.getenv("GPG_SIGNING_PASSWORD")
//    useInMemoryPgpKeys(signingKey, signingPassword)
//    sign(publishing.publications)
//}