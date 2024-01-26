package uk.minersonline.minestom.lobby.stuff.cats;

import net.kyori.adventure.sound.Sound;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.ai.goal.RandomStrollGoal;
import net.minestom.server.MinecraftServer;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.particle.ParticleCreator;
import net.minestom.server.sound.SoundEvent;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CatCreature extends EntityCreature {
    public CatCreature() {
        super(EntityType.CAT);
        addAIGroup(
                List.of(
                    new RandomStrollGoal(this, 20) // Walk around
                ),
                List.of()
        );

        AtomicInteger count = new AtomicInteger(0);
        Scheduler scheduler = MinecraftServer.getSchedulerManager();
        scheduler.submitTask(() -> {
            count.set(count.get() + 1);
            if (count.get() < 10) {
                return TaskSchedule.seconds(1);
            }
            ParticlePacket explosion = ParticleCreator.createParticlePacket(
                    Particle.EXPLOSION,
                    this.position.x(),
                    this.position.y(),
                    this.position.z(),
                    0,
                    0,
                    0,
                    10
            );
            for (Player player : this.instance.getPlayers()){
                player.sendPacket(explosion);
                SoundEvent sound = SoundEvent.fromNamespaceId("minecraft:entity.generic.explode");
                if (sound != null) {
                    player.playSound(Sound.sound(sound.key(),
                        Sound.Source.BLOCK,
                        1.0f,
                        1.0f
                    ),
                        this.position.x(),
                        this.position.y(),
                        this.position.z()
                    );
                }
            }
            this.remove(true);
            return TaskSchedule.stop();
        });
    }
}
