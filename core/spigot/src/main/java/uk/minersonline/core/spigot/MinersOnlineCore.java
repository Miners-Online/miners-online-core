package uk.minersonline.core.spigot;

import net.ME1312.SubServers.Client.Bukkit.SubAPI;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;
import uk.minersonline.core.spigot.display.JoinLeaveManager;

import java.util.logging.Level;

/**
 * The MinersOnlineCore class is the core of the Spigot plugin, it handles initilisation and links everything together, such as commands and events.
 * @since 1.0
 * @author ajh123
 */
public final class MinersOnlineCore extends JavaPlugin {
    private BukkitAudiences adventure;
    private SubAPI subAPI;

    /**
     * This adventure function return an instance to a {@link BukkitAudiences} which allows access to the Advanture text formatting API.
     * 
     * @return a {@link BukkitAudiences} instance if the plugin has been enabled.
     * @throws IllegalStateException if the plugin has not been initilised or it has been disabled.
     * @since 1.0
     * @author ajh123
     */
    public BukkitAudiences adventure() {
        if (this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }

    /**
     * This subAPI function return an instance to a {@link SubAPI} which allows access to the Sub Servers 2 dynamic servers API.
     * 
     * @return a {@link SubAPI} instance if the plugin has been enabled.
     * @throws IllegalStateException if the plugin has not been initilised or it has been disabled.
     * @since 1.0
     * @author ajh123
     */
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
