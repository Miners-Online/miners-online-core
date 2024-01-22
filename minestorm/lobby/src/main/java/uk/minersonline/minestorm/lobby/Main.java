package uk.minersonline.minestorm.lobby;

import net.minestom.server.MinecraftServer;
import net.minestom.server.extras.MojangAuth;


public class Main {
	public static void main(String[] args) {
		// Initialization
		MinecraftServer minecraftServer = MinecraftServer.init();
		MojangAuth.init();
		PlayerInit.init();

		// Start the server on port 25575
		minecraftServer.start("0.0.0.0", 25575);
	}
}