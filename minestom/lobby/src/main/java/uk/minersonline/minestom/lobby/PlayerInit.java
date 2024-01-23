package uk.minersonline.minestom.lobby;

import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.advancements.FrameType;
import net.minestom.server.advancements.notifications.Notification;
import net.minestom.server.advancements.notifications.NotificationCenter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.effects.Effects;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.item.PickupItemEvent;
import net.minestom.server.event.player.*;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.minestom.server.sound.SoundEvent;

public class PlayerInit {
	public static void init() {
		InstanceManager instanceManager = MinecraftServer.getInstanceManager();

		// Create the instance
		InstanceContainer instanceContainer = instanceManager.createInstanceContainer();
		instanceContainer.setChunkSupplier(LightingChunk::new);
		instanceContainer.setChunkLoader(new AnvilLoader("worlds/lobby"));

		GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
		globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
			final Player player = event.getPlayer();
			event.setSpawningInstance(instanceContainer);
			player.setRespawnPoint(new Pos(-264.5, 18, 108.5));
		})
		.addListener(PlayerSpawnEvent.class, event -> {
			if (event.isFirstSpawn()) {
				Notification notification = new Notification(
					Component.text("Welcome to Miners Online!"),
					FrameType.TASK,
					Material.IRON_SWORD
				);
				NotificationCenter.send(notification, event.getPlayer());
				event.getPlayer().sendMessage(Component.text("Welcome to Miners Online!", NamedTextColor.YELLOW));
			}
		})
		.addListener(PlayerBlockBreakEvent.class, event -> event.setCancelled(true))
		.addListener(PlayerBlockInteractEvent.class, event -> event.setCancelled(true))
		.addListener(PlayerBlockPlaceEvent.class, event -> event.setCancelled(true))
		.addListener(PlayerSwapItemEvent.class, event -> event.setCancelled(true))
		.addListener(PlayerUseItemEvent.class, event -> event.setCancelled(true))
		.addListener(PlayerPreEatEvent.class, event -> event.setCancelled(true))
		.addListener(ItemDropEvent.class, event -> event.setCancelled(true))
		.addListener(PickupItemEvent.class, event -> {
			final Entity entity = event.getLivingEntity();
			if (entity instanceof Player) {
				event.setCancelled(true);
			}
		})
		.addListener(PlayerMoveEvent.class, event -> {
			Pos playerPos = event.getPlayer().getPosition();
			if (instanceContainer.getBlock(playerPos.blockX(), playerPos.blockY(), playerPos.blockZ()) == Block.LIGHT_WEIGHTED_PRESSURE_PLATE) {
				Vec vector = playerPos.direction().mul(30.5D).withY(playerPos.y()+10.0D);
				event.getPlayer().setVelocity(vector);
				event.getPlayer().playEffect(
						Effects.BAT_TAKES_OFF,
						playerPos.blockX(), playerPos.blockY(), playerPos.blockZ(), 0, true);
				event.getPlayer().playSound(Sound.sound(
						SoundEvent.fromNamespaceId("minecraft:entity.bat.takeoff").key(),
						Sound.Source.BLOCK,
						1.0f,
						1.0f
				));
			}
		});
	}
}
