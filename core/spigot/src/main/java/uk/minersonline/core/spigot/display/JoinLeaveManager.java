package uk.minersonline.core.spigot.display;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import uk.minersonline.core.spigot.MinersOnlineCore;

/**
 * The JoinLeaveManager class listens for player join and quit events. This class will clear existing status messages and produce its own.
 * 
 * @since 1.0
 * @author ajh123
 */
public class JoinLeaveManager implements Listener {
	private final MinersOnlineCore plugin;

	public JoinLeaveManager(MinersOnlineCore plugin) {
		this.plugin = plugin;
	}

	/**
	 * The onPlayerJoin function is an event handler that listens for the {@link PlayerJoinEvent}, this function is the actual implementation for
	 * custom join messages.
	 * 
	 * @param event The {@link PlayerJoinEvent} instance, providing detials of who joined the server.
	 * @since 1.0
	 * @author ajh123
	 */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);
		plugin.getServer().broadcastMessage(ChatColor.DARK_GREEN + "+ "+ChatColor.GRAY+event.getPlayer().getPlayerListName());
	}

	/**
	 * The onPlayerJoin function is an event handler that listens for the {@link PlayerQuitEvent}, this function is the actual implementation for
	 * custom leave messages.
	 * 
	 * @param event The {@link PlayerQuitEvent} instance, providing detials of who left the server.
	 * @since 1.0
	 * @author ajh123
	 */
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);
		plugin.getServer().broadcastMessage(ChatColor.RED + "- "+ChatColor.GRAY+event.getPlayer().getPlayerListName());
	}
}
