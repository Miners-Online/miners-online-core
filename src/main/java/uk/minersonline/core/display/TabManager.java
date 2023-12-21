package uk.minersonline.core.display;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import uk.minersonline.core.MinersOnlineCore;

public class TabManager implements Listener {
	private final MinersOnlineCore plugin;

	public TabManager(MinersOnlineCore plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		BukkitAudiences audience = plugin.adventure();
		Player player = event.getPlayer();
		if (audience != null) {
			String server = plugin.subAPI().getRemotePlayer(player.getUniqueId()).getServerName();
			Audience playerAudience = audience.player(player);
			final Component header = Component.text("<center>Miners Online</center>", NamedTextColor.GOLD);
			final Component footer = Component.text("You are on "+server, NamedTextColor.AQUA);
			playerAudience.sendPlayerListHeaderAndFooter(header, footer);
		}
	}
}
