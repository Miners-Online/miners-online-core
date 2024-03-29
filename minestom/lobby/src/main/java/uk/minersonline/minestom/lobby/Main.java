package uk.minersonline.minestom.lobby;

import net.hollowcube.minestom.extensions.ExtensionBootstrap;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.extras.bungee.BungeeCordProxy;
import net.minestom.server.extras.velocity.VelocityProxy;
import org.jetbrains.annotations.NotNull;
import uk.minersonline.minestom.lobby.stuff.cats.MoreCats;
import uk.minersonline.minestom.lobby.stuff.people.People;


public class Main {
	public static void main(String[] args) {
		// Settings
		Settings.read();
		if (Settings.isTerminalDisabled())
			System.setProperty("minestom.terminal.disabled", "");

		// Initialization
		ExtensionBootstrap server = ExtensionBootstrap.init();
		MinecraftServer.setBrandName("minersonline");
		MapModInit.init();
		PlayerInit.init();
		People.init();
		MoreCats.init();

		switch (Settings.getMode()) {
			case OFFLINE -> {}
			case ONLINE -> MojangAuth.init();
			case BUNGEECORD -> BungeeCordProxy.enable();
			case VELOCITY -> {
				if (!Settings.hasVelocitySecret())
					throw new IllegalArgumentException("The velocity secret is mandatory.");
				VelocityProxy.enable(Settings.getVelocitySecret());
			}
		}

		MinecraftServer.LOGGER.info("Running in " + Settings.getMode() + " mode.");
		MinecraftServer.LOGGER.info("Listening on " + Settings.getServerIp() + ":" + Settings.getServerPort());

		// Start server
		server.start(Settings.getServerIp(), Settings.getServerPort());
	}
}