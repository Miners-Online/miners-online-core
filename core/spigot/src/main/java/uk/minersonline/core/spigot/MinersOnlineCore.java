package uk.minersonline.core.spigot;

import net.ME1312.SubServers.Client.Bukkit.SubAPI;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;
import uk.minersonline.core.spigot.display.JoinLeaveManager;

import java.util.logging.Level;

public final class MinersOnlineCore extends JavaPlugin {
    private BukkitAudiences adventure;
    private SubAPI subAPI;

    public BukkitAudiences adventure() {
        if (this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }

    public SubAPI subAPI() {
        if (this.subAPI == null) {
            throw new IllegalStateException("Tried to access SubAPI when the plugin was disabled!");
        }
        return this.subAPI;
    }

    @Override
    public void onEnable() {
        // API startup logic
        this.adventure = BukkitAudiences.create(this);
        this.subAPI = SubAPI.getInstance();

        // Plugin startup logic

        // Event handlers
        getServer().getPluginManager().registerEvents(new JoinLeaveManager(this), this);
        // Plugin channels
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");


        getLogger().log(Level.INFO, "Miners Online Core loaded successfully!");
    }

    @Override
    public void onDisable() {
        // API shutdown logic
        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
        if (this.subAPI != null) {
            this.subAPI = null;
        }

        // Plugin shutdown logic
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
    }
}
