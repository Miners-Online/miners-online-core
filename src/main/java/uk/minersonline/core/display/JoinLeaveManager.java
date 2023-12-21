package uk.minersonline.core.display;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import uk.minersonline.core.MinersOnlineCore;

public class JoinLeaveManager implements Listener {
	private final MinersOnlineCore plugin;

	public JoinLeaveManager(MinersOnlineCore plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);
		plugin.getServer().broadcastMessage(ChatColor.DARK_GREEN + "+ "+ChatColor.GRAY+event.getPlayer().getPlayerListName());
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);
		plugin.getServer().broadcastMessage(ChatColor.RED + "- "+ChatColor.GRAY+event.getPlayer().getPlayerListName());
	}
}
