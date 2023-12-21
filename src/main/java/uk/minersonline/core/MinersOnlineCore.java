package uk.minersonline.core;

import net.ME1312.SubServers.Client.Bukkit.SubAPI;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;
import uk.minersonline.core.command.LobbyCommand;
import uk.minersonline.core.display.TabManager;

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
        getServer().getPluginManager().registerEvents(new TabManager(this), this);
        this.getCommand("lobby").setExecutor(new LobbyCommand(this));
        this.getCommand("hub").setExecutor(new LobbyCommand(this));
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
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
