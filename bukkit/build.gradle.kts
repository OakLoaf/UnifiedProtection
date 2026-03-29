dependencies {
    // Dependencies
    compileOnly("org.spigotmc:spigot-api:26.1-R0.1-SNAPSHOT")

    // Soft Dependencies
    compileOnly("com.github.TechFortress:GriefPrevention:18.0.0")
    compileOnly("net.william278.huskclaims:huskclaims-bukkit:1.5.10")
    compileOnly("org.lushplugins:PvPToggle:2.0.0")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.16") {
        exclude("com.google.guava", "guava")
        exclude("com.google.code.gson", "gson")
    }

    // Libraries
    api(project(":common"))
}
