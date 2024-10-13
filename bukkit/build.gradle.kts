dependencies {
    // Dependencies
    compileOnly("org.spigotmc:spigot-api:1.21.1-R0.1-SNAPSHOT")

    // Soft Dependencies
    compileOnly("com.github.TechFortress:GriefPrevention:17.0.0")
    compileOnly("net.william278.husktowns:husktowns-bukkit:3.0.6")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.12")

    // Libraries
    implementation(project(":common"))
}
