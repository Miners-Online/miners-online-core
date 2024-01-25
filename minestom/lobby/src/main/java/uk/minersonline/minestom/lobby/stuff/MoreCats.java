package uk.minersonline.minestom.lobby.stuff;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.condition.Conditions;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerChatEvent;

import java.util.*;

/**
 * This is a funny class that asks the player for the number of cats that they want.
 * The player will type multiple messages of "yes" in the chat and increment the count of cats.
 * The program will keep track of the number of cats and will output the count when the user
 * chats anything that is not "yes".
 */
public class MoreCats extends Command {
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

	public enum CatCommandMode {
		ASK("ask"),
		SET("set"),
		RESET("reset"),
		SUMMON("summon");

		private final String name;

		CatCommandMode(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return this.name;
		}
	}

	public MoreCats() {
		super("cats");

		// Make this command can only be run by players.
		setCondition(Conditions::playerOnly);
		// If no arguments are provided we want default code to run.
		setDefaultExecutor((sender, context) -> {
			String commandName = context.getCommandName();
			sender.sendMessage(Component.text("Usage: /" + commandName + " <ASK|RESET|SET> [cat_count]", NamedTextColor.RED));
		});

		// Create an enum argument
		var commandNode = ArgumentType.Enum("mode", CatCommandMode.class);
		addSyntax((sender, context) -> {
			switch (context.get(commandNode)) {
				case ASK -> {
					if (sender instanceof Player player) {
						player.sendMessage("How many cats do you want? Please chat \"yes\" multiple times for the number of cats you want.");
						isRunning.put(player.getUuid(), true);
						if (!catCount.containsKey(player.getUuid())) {
							catCount.put(player.getUuid(), 0);
						}
					}
				}
				case SET -> {
					String commandName = context.getCommandName();
					sender.sendMessage(Component.text("Required parameter `cat_count` is missing, Usage: /" + commandName + " SET [cat_count]", NamedTextColor.RED));
				}
				case RESET -> {
					if (sender instanceof Player player) {
						player.sendMessage("Reset your cat count, you now have 0.");
						catCount.put(player.getUuid(), 0);
					}
				}
				case SUMMON -> {
					if (sender instanceof Player player) {
						if (!catCount.containsKey(player.getUuid())) {
							sender.sendMessage(Component.text("You need more then 0 cats.", NamedTextColor.RED));
						} else {
							int curCatCount = catCount.get(player.getUuid());
							isRunning.put(player.getUuid(), false);
							player.sendMessage("Summoned " + curCatCount + " cat(s)");
							summonCats(player, curCatCount);
							player.sendMessage("Reset your cat count, you now have 0.");
							catCount.put(player.getUuid(), 0);
						}
					}
				}
			}
		}, commandNode);

		commandNode.setCallback((sender, exception) -> {
			final String input = exception.getInput();
			sender.sendMessage(Component.text("The mode " + input + " is invalid!", NamedTextColor.RED));
			sender.sendMessage(Component.text("Please choose `ASK`, `SET`, or `RESET`.", NamedTextColor.RED));
		});

		// Create an integer argument
		var newCats = ArgumentType.Integer("cat_count");
		// Add code to be run when an integer is provided.
		addSyntax((sender, context) -> {
			if (context.get(commandNode) == CatCommandMode.SET) {
				// Get the integer from the user.
				final int newCatCount = context.get(newCats);
				// Check if a player has run the command.
				if (sender instanceof Player player) {
					// Append the new count to the current count.
					catCount.put(player.getUuid(), newCatCount);
					// Create a message telling the user the new cats.
					player.sendMessage("You now have " + newCatCount + " cat(s)");
				}
			} else {
				sender.sendMessage(Component.text("The mode " + context.get(commandNode) + " is invalid!", NamedTextColor.RED));
				sender.sendMessage(Component.text("You must use `SET`.", NamedTextColor.RED));
			}
		}, commandNode, newCats);

		newCats.setCallback((sender, exception) -> {
			final String input = exception.getInput();
			sender.sendMessage(Component.text("The number " + input + " is invalid!", NamedTextColor.RED));
		});
	}

	/**
	 * The init method initialises the MoreCats class, this method will add event handlers to
	 * the server.
	 */
	public static void init() {
		CommandManager commandManager = MinecraftServer.getCommandManager();
		commandManager.register(new MoreCats());

		GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
		// This event handler will listen for chat messages from the player.
		globalEventHandler.addListener(PlayerChatEvent.class, event -> {
			// We only want to count cats if the player wants to.
			if (isRunning.containsKey(event.getPlayer().getUuid()) && isRunning.get(event.getPlayer().getUuid())) {
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

	private static void summonCats(Player player, int count) {
		for (int cats = 0; cats <= count; cats++) {
			var cat = new EntityCreature(EntityType.CAT);
			cat.setCustomName(player.getName().append(Component.text("'s cat")));
			cat.setInvulnerable(false);
			cat.setInstance(player.getInstance(), player.getPosition());
		}
	}
}
