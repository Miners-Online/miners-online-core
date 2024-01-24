package uk.minersonline.minestom.lobby.stuff;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerChatEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;

import java.util.*;

/**
 * This is a funny class that asks the player for the number of cats that they want.
 * The player will type multiple messages of "yes" in the chat and increment the count of cats.
 * The program will keep track of the number of cats and will output the count when the user
 * chats anything that is not "yes".
 */
public class MoreCats {
	/**
	 * The catCount map keeps track of the number of cats the player wants. It is a map because
	 * there can be multiple players on a Minecraft server at once.
	 */
	private final static Map<UUID, Integer> catCount = new HashMap<>();
	/**
	 * The isRunning map keeps tracks weather the user wants more cats. It is a map because
	 * there can be multiple players on a Minecraft server at once.
	 */
	private final static Map<UUID, Boolean> isRunning = new HashMap<>();

	/**
	 * The init method initialises the MoreCats class, this method will add event handlers to
	 * the server.
	 */
	public static void init() {
		GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

		// When the player connects will ask them how many cats do they want.
		globalEventHandler.addListener(PlayerSpawnEvent.class, event -> {
			event.getPlayer().sendMessage("How many cats do you want? Please chat \"yes\" multiple times for the number of cats you want.");
			isRunning.put(event.getPlayer().getUuid(), true);
		});

		// This event handler will listen for chat messages from the player.
		globalEventHandler.addListener(PlayerChatEvent.class, event -> {
			// We only want to count cats if the player wants to.
			if (isRunning.get(event.getPlayer().getUuid())) {
				// Cancel any chat messages so there is no spam.
				event.setCancelled(true);
				// Get the message from the player.
				String answer = event.getMessage();
				// Check if the answer is "yes".
				if (answer.equalsIgnoreCase("yes")) {
					// Check if the player does not have a count.
					if (!catCount.containsKey(event.getPlayer().getUuid())) {
						// If so, set the count to 1.
						catCount.put(event.getPlayer().getUuid(), 1);
					} else {
						// Else, get the current count.
						int curCatCount = catCount.get(event.getPlayer().getUuid());
						// Then add one and put it back.
						catCount.put(event.getPlayer().getUuid(), curCatCount + 1);
					}
					// Tell the player we have added one cat.
					event.getPlayer().sendMessage("Added one cat");
				} else {
					// When the user does not want more cats, we want to output the final count.
					int curCatCount = catCount.get(event.getPlayer().getUuid());
					isRunning.put(event.getPlayer().getUuid(), false);
					event.getPlayer().sendMessage("You now have "+curCatCount+" cat(s)");
				}
			}
		});
	}
}
