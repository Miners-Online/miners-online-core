package uk.minersonline.core.velocity.command;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerPing;

import net.ME1312.SubServers.Client.Common.Network.API.Server;
import net.ME1312.SubServers.Client.Common.Network.API.SubServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import uk.minersonline.core.velocity.MinersOnlineCore;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class LobbyCommand implements SimpleCommand {
	private final MinersOnlineCore plugin;

	public LobbyCommand(MinersOnlineCore plugin) {
		this.plugin = plugin;
	}

	@Override
	public void execute(final Invocation invocation) {
		if (invocation.source() instanceof Player player) {
			if (player.hasPermission("minersonline.commands.lobby")) {
				// Get the lobby group
				this.plugin.subAPI().getGroup("Lobby", (groupServers) -> {
					// Get all lobby servers
					Collection<? extends Server> servers = groupServers.value();
					// Pick a random lobby
					matchServer(servers, player);
				});
				return;
			}
		}
		invocation.source().sendMessage(Component.text("You are not allowed to use this command.").color(NamedTextColor.RED));
		return;
	}

	@Override
	public boolean hasPermission(final Invocation invocation) {
		return invocation.source().hasPermission("minersonline.commands.lobby");
	}

	public static <T> T random(Collection<T> coll) {
		int num = (int) (Math.random() * coll.size());
		for(T t: coll) if (--num < 0) return t;
		throw new AssertionError();
	}

	public void matchServer(Collection<? extends Server> servers, Player player) {
		// Send message to player
		player.sendMessage(Component.text("Finding a lobby...").color(NamedTextColor.YELLOW));
		boolean found = false;
		AtomicReference<Server> server = new AtomicReference<>();
		while (!found) {
			Server serverFound = random(servers);
			// Refresh server data
			serverFound.refresh();
			player.sendMessage(Component.text("Trying "+serverFound.getName()).color(NamedTextColor.YELLOW));
			if (serverFound instanceof SubServer subServer) {
				// Check if server is online
				if (subServer.isOnline()) {
					found = true;
					server.set(serverFound);
				}
			} else {
				Optional<RegisteredServer> registeredServer = plugin.proxy().getServer(serverFound.getName());
				// Check if server exists
				if (registeredServer.isPresent()) {
					RegisteredServer registeredServer2 = registeredServer.get();
					// Check if server is online
					try {
						ServerPing ping = registeredServer2.ping().get();
						if (ping != null) {
							found = true;
							// We have found a valid server, set the reference
							server.set(serverFound);
						}
					} catch (Exception e) {
						plugin.logger().error("An error occurred whilst checking a server", e);
					}
				}
			}
			if (!found) {
				// Display result
				player.sendMessage(Component.text(server.get().getName() + " is not online. Finding another...").color(NamedTextColor.YELLOW));
			}
		}

		// Check if we have found a server
		if (server.get() != null) {
			plugin.subAPI().getRemotePlayer(player.getUsername(), (remotePlayer) -> {
				remotePlayer.getServer((playerServer) -> {
					// Get the groups of the server the player is connected to
					List<String> groups = playerServer.getGroups();
					// Check if the player is connected to a Lobby
					if (groups.contains("Lobby")) {
						player.sendMessage(Component.text("You are already connected to a lobby.").color(NamedTextColor.RED));
						return;
					}
					// Send message to player
					player.sendMessage(Component.text("Teleporting to "+server.get().getName()).color(NamedTextColor.GREEN));
					// Create a task to run later
					plugin.proxy().getScheduler()
					.buildTask(plugin, () -> {
						remotePlayer.transfer(server.get().getName());
					})
					.repeat(2L, TimeUnit.SECONDS)
					.schedule();
				});
			});
			return;
		}
		player.sendMessage(Component.text("No lobbies are online, try again later.").color(NamedTextColor.YELLOW));
	}
}
