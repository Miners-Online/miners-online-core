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
				this.plugin.subAPI().getGroup("Lobby", (groupServers) -> {
					Collection<? extends Server> servers = groupServers.value();
					Server server = random(servers);
					server.refresh();
					player.sendMessage(ChatColor.YELLOW + "Teleporting to "+server.getName());

					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
						ByteArrayDataOutput out = ByteStreams.newDataOutput();
						out.writeUTF("Connect");
						out.writeUTF(server.getName());
						player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
					}, 20L * 2);
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
