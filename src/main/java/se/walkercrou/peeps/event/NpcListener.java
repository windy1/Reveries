package se.walkercrou.peeps.event;

import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.filter.cause.First;
import se.walkercrou.peeps.event.npc.NpcLoseSightOfPlayerEvent;
import se.walkercrou.peeps.event.npc.NpcSpotPlayerEvent;

public final class NpcListener {

    @Listener
    public void onPlayerSpot(NpcSpotPlayerEvent event, @Getter("getTargetEntity") Living npc, @First Player player) {
        System.out.println("NPC <" + npc.getUniqueId() + "> spotted " + player.getName() + ".");
    }

    @Listener
    public void onPlayerSightLost(NpcLoseSightOfPlayerEvent event,
                                  @Getter("getTargetEntity") Living npc,
                                  @First Player player) {
        System.out.println("NPC <" + npc.getUniqueId() + "> lost sight of " + player.getName() + ".");
    }

}
