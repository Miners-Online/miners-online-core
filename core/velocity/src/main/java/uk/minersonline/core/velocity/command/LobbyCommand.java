package uk.minersonline.core.velocity.command;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import net.ME1312.SubServers.Client.Common.Network.API.Server;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import uk.minersonline.core.velocity.MinersOnlineCore;


import java.util.Collection;
import java.util.concurrent.TimeUnit;

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
					Server server = random(servers);
					// Refresh server data
					server.refresh();
					// Send message to player
					player.sendMessage(Component.text("Teleporting to "+server.getName()).color(NamedTextColor.YELLOW));

					// Create a task to run later
					plugin.proxy().getScheduler()
					.buildTask(plugin, () -> {
						Collection<RegisteredServer> registeredServers = plugin.proxy().matchServer(server.getName());
						RegisteredServer found = (RegisteredServer) registeredServers.toArray()[0];
						player.createConnectionRequest(found).connectWithIndication()            ;
					})
					.repeat(3L, TimeUnit.MINUTES)
					.schedule();
				});
			}
		}
		invocation.source().sendMessage(Component.text("You are not allowed to use this command.").color(NamedTextColor.RED));
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
}
