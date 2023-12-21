package uk.minersonline.core.command;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.ME1312.SubServers.Client.Common.Network.API.Server;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import uk.minersonline.core.MinersOnlineCore;

import java.util.Collection;

public class LobbyCommand implements CommandExecutor {
	private final MinersOnlineCore plugin;

	public LobbyCommand(MinersOnlineCore plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (sender instanceof Player player) {
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
					player.sendMessage(ChatColor.YELLOW + "Teleporting to "+server.getName());

					// Create a task to run later
					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
						// Create a byte array to store a message
						ByteArrayDataOutput out = ByteStreams.newDataOutput();
						// Write "Connect" to tell the proxy we want to forward the player
						out.writeUTF("Connect");
						// Tell the proxy the server we want to connect to
						out.writeUTF(server.getName());
						// Send the message to the player on the "BungeeCord" channel (which will be intercepted
						// by the proxy)
						player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
					}, 3L); // Schedule the task 3 ticks later (so the player can read the message
				});
				return true;
			}
		}
		sender.sendMessage(ChatColor.RED + "You are not allowed to use this command.");
		return false;
	}

	public static <T> T random(Collection<T> coll) {
		int num = (int) (Math.random() * coll.size());
		for(T t: coll) if (--num < 0) return t;
		throw new AssertionError();
	}
}
