package se.walkercrou.peeps;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.collect.Sets;
import org.spongepowered.api.data.property.AbstractProperty;
import org.spongepowered.api.data.property.entity.EyeLocationProperty;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.scheduler.Task;
import se.walkercrou.peeps.data.npc.NpcData;
import se.walkercrou.peeps.event.npc.NpcLoseSightOfPlayerEvent;
import se.walkercrou.peeps.event.npc.NpcSpotPlayerEvent;
import se.walkercrou.peeps.trait.NpcTrait;
import se.walkercrou.peeps.trait.NpcTraits;

import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class NpcMonitor implements Consumer<Task> {

    private final Peeps plugin;
    private final Living npc;
    private final Set<Player> visible = Sets.newHashSet();
    private Player tracking;

    boolean preview = false;

    public NpcMonitor(Peeps plugin, Living npc) {
        this.plugin = plugin;
        this.npc = npc;
    }

    @Override
    public void accept(Task task) {
        if (this.npc.isRemoved()) {
            task.cancel();
            return;
        }

        double sightRange = this.npc.get(NpcData.class).get().sightRange().get();
        if (sightRange > 0) {
            Vector3d pos = this.npc.getLocation().getPosition();
            Set<Player> visiblePlayers = this.npc.getWorld().getEntities(e ->
                e instanceof Player && pos.distance(e.getLocation().getPosition()) <= sightRange).stream()
                .map(e -> (Player) e).collect(Collectors.toSet());

            Set<Player> toAdd = Sets.newHashSet();
            Set<Player> toRemove = Sets.newHashSet();

            for (Player player : visiblePlayers) {
                if (!this.visible.contains(player)) {
                    boolean cancelled = this.plugin.game.getEventManager().post(new NpcSpotPlayerEvent(
                        this.npc, Cause.source(this.plugin.self).owner(player).build()));
                    if (!cancelled)
                        toAdd.add(player);
                }
            }

            for (Player player : this.visible) {
                if (!visiblePlayers.contains(player)) {
                    boolean cancelled = this.plugin.game.getEventManager().post(new NpcLoseSightOfPlayerEvent(
                        this.npc, Cause.source(this.plugin.self).owner(player).build()));
                    if (!cancelled)
                        toRemove.add(player);
                }
            }

            this.visible.addAll(toAdd);
            this.visible.removeAll(toRemove);

            Set<NpcTrait> traits = this.npc.get(NpcData.class).get().traits().get();
            if (traits.contains(NpcTraits.HEAD_TRACKING)) {
                if (this.tracking == null)
                    this.tracking = this.visible.stream().findAny().orElse(null);
                else if (!this.visible.contains(this.tracking))
                    this.tracking = null;

                if (this.tracking != null) {
                    this.npc.lookAt(this.tracking.getProperty(EyeLocationProperty.class)
                        .map(AbstractProperty::getValue).orElse(this.tracking.getLocation().getPosition()));
                }
            }
        }
    }

}
