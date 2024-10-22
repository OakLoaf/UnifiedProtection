plugins {
    `java-library`
    `maven-publish`
    id("io.github.goooler.shadow") version("8.1.7")
}

group = "org.lushplugins"
version = "1.0.0-alpha3"

dependencies {
    api(project(":bukkit"))
}

tasks {
    shadowJar {
        relocate("net.william278.cloplib", "org.lushplugins.libraries.cloplib")

        minimize()

        archiveFileName.set("${project.name}-${project.version}.jar")
    }
}

allprojects {
    apply(plugin="java-library")

    group = rootProject.group
    version = rootProject.version

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

    dependencies {
        compileOnly("org.jetbrains:annotations:26.0.1")
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
            groupId = rootProject.group.toString()
            artifactId = rootProject.name
            version = rootProject.version.toString()
            from(project.components["java"])
        }
    }
}