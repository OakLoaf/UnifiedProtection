plugins {
    `java-library`
    `maven-publish`
    id("io.github.goooler.shadow") version("8.1.7")
}

group = "org.lushplugins"
version = "1.0.0-alpha16"

allprojects {
    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://oss.sonatype.org/content/groups/public/")
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") // Spigot
        maven("https://repo.william278.net/releases") // ClopLib, HuskClaims, HuskTowns
        maven("https://repo.lushplugins.org/snapshots/") // PvPToggle
        maven("https://maven.enginehub.org/repo/") // WorldGuard
        maven("https://jitpack.io") // GriefPrevention
    }
}

subprojects {
    apply(plugin="java-library")
    apply(plugin="maven-publish")
    apply(plugin="io.github.goooler.shadow")

    group = rootProject.group
    version = rootProject.version

    dependencies {
        compileOnly("org.jetbrains:annotations:26.0.2")
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))

        registerFeature("optional") {
            usingSourceSet(sourceSets["main"])
        }

        withSourcesJar()
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }

        build {
            dependsOn(shadowJar)
        }

        shadowJar {
            relocate("net.william278.cloplib", "org.lushplugins.unifiedprotection.libraries")

            minimize()

            archiveFileName.set("${project.name}-${project.version}.jar")
        }
    }

    publishing {
        repositories {
            maven {
                name = "lushReleases"
                url = uri("https://repo.lushplugins.org/releases")
                credentials(PasswordCredentials::class)
                authentication {
                    isAllowInsecureProtocol = true
                    create<BasicAuthentication>("basic")
                }
            }

            maven {
                name = "lushSnapshots"
                url = uri("https://repo.lushplugins.org/snapshots")
                credentials(PasswordCredentials::class)
                authentication {
                    isAllowInsecureProtocol = true
                    create<BasicAuthentication>("basic")
                }
            }
        }

        publications {
            create<MavenPublication>("maven") {
                groupId = rootProject.group.toString() + ".unifiedprotection"
                artifactId = rootProject.name + "-" + project.name
                version = rootProject.version.toString()
                from(project.components["java"])
            }
        }
    }
}