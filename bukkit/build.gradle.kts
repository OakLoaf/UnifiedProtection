dependencies {
    // Dependencies
    compileOnly("org.spigotmc:spigot-api:1.21.5-R0.1-SNAPSHOT")

    // Soft Dependencies
    compileOnly("com.github.TechFortress:GriefPrevention:17.0.0")
    compileOnly("net.william278.huskclaims:huskclaims-bukkit:1.5.8")
    compileOnly("org.lushplugins:PvPToggle:2.0.0")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.13") {
        exclude("com.google.guava", "guava")
        exclude("com.google.code.gson", "gson")
    }

    // Libraries
    api(project(":common"))
}
