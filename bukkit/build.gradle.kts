dependencies {
    // Dependencies
    compileOnly("org.spigotmc:spigot-api:1.21.2-R0.1-SNAPSHOT")

    // Soft Dependencies
    compileOnly("com.github.TechFortress:GriefPrevention:17.0.0")
    compileOnly("net.william278.husktowns:husktowns-bukkit:3.0.6")
    compileOnly("org.lushplugins:PvPToggle:2.0.0-beta8")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.12")

    // Libraries
    api(project(":common"))
}
