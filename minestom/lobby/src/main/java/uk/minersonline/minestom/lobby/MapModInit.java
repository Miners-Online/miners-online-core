package uk.minersonline.minestom.lobby;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.*;

public class MapModInit {
	public static void init() {
		GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
		globalEventHandler.addListener(PlayerSpawnEvent.class, event -> {
			event.getPlayer().sendPluginMessage("xaerominimap:main", Serialisation.serialise("lobby".hashCode()));
			event.getPlayer().sendPluginMessage("xaeroworldmap:main", Serialisation.serialise("lobby".hashCode()));
		});
	}
}
