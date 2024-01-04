package uk.minersonline.core.velocity;

import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import org.slf4j.Logger;
import net.ME1312.SubServers.Velocity.SubAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import uk.minersonline.core.velocity.command.LobbyCommand;

@Plugin(
		id = "miners_online_core",
		name = "MinersOnlineCore",
		version = BuildConstants.VERSION,
		description = "The core plugin for the Miners Online server",
		url = "https://minersonline.uk",
		authors = {"ajh123"}
)
public class MinersOnlineCore {
	private SubAPI subAPI;
    private final ProxyServer proxy;
	private final Logger logger;

    public MinersOnlineCore(ProxyServer proxy, Logger logger) {
        this.proxy = proxy;
		this.logger = logger;
    }

	public SubAPI subAPI() {
        if (this.subAPI == null) {
            throw new IllegalStateException("Tried to access SubAPI when the plugin was disabled!");
        }
        return this.subAPI;
    }

	public ProxyServer proxy() {
        return this.proxy;
    }

	public Logger logger() {
        return this.logger;
    }

	@Subscribe
	public void onProxyInitialization(ProxyInitializeEvent event) {
		// API startup logic
		this.subAPI = SubAPI.getInstance();

		// Plugin startup logic

		// Command handlers
		CommandManager commandManager = proxy.getCommandManager();

        CommandMeta commandMeta = commandManager.metaBuilder("lobby")
            .aliases("hub")
            .plugin(this)
            .build();

		commandManager.register(commandMeta, new LobbyCommand(this));
	}

	@Subscribe
	public void onProxyShutdownEvent(ProxyShutdownEvent event) {
		// API shutdown logic
		if (this.subAPI != null) {
			this.subAPI = null;
		}
	}

	@Subscribe
	public void onServerConnected(ServerConnectedEvent event) {
		Player player = event.getPlayer();
		this.subAPI().getRemotePlayer(player.getUsername(), (remotePlayer) -> {
			final Component header = Component.text("Miners Online", NamedTextColor.GOLD);
			final Component footer = Component.text("You are on "+remotePlayer.getServerName(), NamedTextColor.AQUA);
			player.getTabList().clearHeaderAndFooter();
			player.sendPlayerListHeaderAndFooter(header, footer);
		});
	}
}
