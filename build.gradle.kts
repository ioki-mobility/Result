plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.dokka)
    `maven-publish`
    signing
}

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()
    jvmToolchain(17)

    applyDefaultHierarchyTemplate()

    jvm()
    iosX64()
    iosArm64()
    macosX64()
    macosArm64()
    linuxX64()
    linuxArm64()
    mingwX64()

    sourceSets {
        val jvmTest by getting {
             dependencies {
                implementation(libs.test.junit)
                implementation(libs.test.strikt)
             }
         }
    }
}

val dokkaJar = tasks.register<Jar>("dokkaJar") {
    dependsOn(tasks.dokkaGenerate)
    from(tasks.dokkaGeneratePublicationHtml.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

publishing {
    publications {
        withType<MavenPublication> {
            artifact(dokkaJar)

            groupId = "com.ioki.result"
            artifactId = artifactId.lowercase()
            version = "0.4.0-SNAPSHOT"

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
                        id.set("ioki")
                        name.set("ioki Android Team")
                        organization.set("ioki")
                        organizationUrl.set("https://www.ioki.com")
                    }
                }
                scm {
                    url.set("https://github.com/ioki-mobility/Result")
                    connection.set("scm:git:git://github.com/ioki-mobility/Result.git")
                    developerConnection.set("scm:git:ssh://git@github.com:ioki-mobility/Result.git")
                }
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

signing {
    val signingKey = System.getenv("GPG_SIGNING_KEY")
    val signingPassword = System.getenv("GPG_SIGNING_PASSWORD")
    isRequired = hasProperty("GPG_SIGNING_REQUIRED")
    if (isRequired) useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications)
}

// Workaround taken from here:
// https://github.com/gradle/gradle/issues/26091#issuecomment-1722947958
// Maybe fix can be found here:
// https://github.com/gradle/gradle/pull/26292
tasks.withType<AbstractPublishToMaven>().configureEach {
    val signingTasks = tasks.withType<Sign>()
    mustRunAfter(signingTasks)
}
