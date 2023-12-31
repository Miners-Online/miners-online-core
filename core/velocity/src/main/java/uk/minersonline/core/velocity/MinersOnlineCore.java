package uk.minersonline.core.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import net.ME1312.Galaxi.Library.Container.Pair;
import net.ME1312.SubServers.Client.Common.Network.API.Server;
import org.slf4j.Logger;
import net.ME1312.SubServers.Velocity.SubAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import uk.minersonline.core.velocity.command.LobbyCommand;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

/**
 * The MinersOnlineCore class is the core of the Velocity plugin, it handles initilisation and links everything together, such as commands and events.
 * @since 1.0
 * @author ajh123
 */
@Plugin(
		id = "miners_online_core",
		name = "MinersOnlineCore",
		version = BuildConstants.VERSION,
		description = "The core plugin for the Miners Online server",
		url = "https://minersonline.uk",
		authors = {"ajh123"},
		dependencies = {
				@Dependency(id = "subservers-sync")
		}
)
public class MinersOnlineCore {
	private SubAPI subAPI;
    private final ProxyServer proxy;
	private final Logger logger;

	@Inject
    public MinersOnlineCore(ProxyServer proxy, Logger logger) {
        this.proxy = proxy;
		this.logger = logger;
    }


    /**
     * This subAPI function returns an instance to a {@link SubAPI} which allows access to the Sub Servers 2 dynamic servers API.
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

    /**
     * This proxy function returns an instance to a {@link ProxyServer} which allows access to information about the current proxy.
     * 
     * @return a {@link ProxyServer} instance.
     * @since 1.0
	 * @author ajh123
	 */
	public ProxyServer proxy() {
        return this.proxy;
    }

    /**
     * This logger function returns an instance to a {@link Logger} which allows access to debug logging.
     * 
     * @return a {@link Logger} instance.
	 * @since 1.0
	 * @author ajh123
	 */
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
			if (remotePlayer == null) {
				this.subAPI().getServers((servers) -> {
					for (Server server : servers.values()) {
						Collection<Pair<String, UUID>> players = server.getRemotePlayers();
						for (Pair<String, UUID> pl : players) {
							if (Objects.equals(pl.key(), player.getUsername())) {
								final Component header = Component.text("Miners Online", NamedTextColor.GOLD);
								final Component footer = Component.text("You are on " + server.getName(), NamedTextColor.AQUA);
								player.getTabList().clearHeaderAndFooter();
								player.sendPlayerListHeaderAndFooter(header, footer);
								return;
							}
						}
					}
				});
			} else {
				final Component header = Component.text("Miners Online", NamedTextColor.GOLD);
				final Component footer = Component.text("You are on " + remotePlayer.getServerName(), NamedTextColor.AQUA);
				player.getTabList().clearHeaderAndFooter();
				player.sendPlayerListHeaderAndFooter(header, footer);
			}
		});
	}
}
