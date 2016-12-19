package se.walkercrou.peeps;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.collect.Sets;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.property.AbstractProperty;
import org.spongepowered.api.data.property.entity.EyeLocationProperty;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.MutableBlockVolume;
import se.walkercrou.peeps.data.mutable.NpcData;
import se.walkercrou.peeps.data.mutable.SightData;
import se.walkercrou.peeps.event.npc.NpcLoseSightOfPlayerEvent;
import se.walkercrou.peeps.event.npc.NpcSpotPlayerEvent;
import se.walkercrou.peeps.trait.NpcTrait;
import se.walkercrou.peeps.trait.NpcTraits;

import java.util.Optional;
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

        Optional<SightData> sightData = this.npc.get(SightData.class);
        if (sightData.isPresent()) {
            SightData data = sightData.get();
            Transform<World> transform = this.npc.getTransform();
            Direction direction = Direction.getClosest(transform.getRotationAsQuaternion().getDirection());

            MutableBlockVolume frontFov = getFieldOfView(direction, data.frontSightRange().get());
            MutableBlockVolume backFov = getFieldOfView(direction.getOpposite(), data.backSightRange().get());

            //renderFovPreview(frontView);
            renderFovPreview(backFov);

            Set<Player> visiblePlayers = transform.getExtent().getEntities(e -> {
                Vector3i position = e.getLocation().getBlockPosition();
                return e instanceof Player && (frontFov.containsBlock(position) || backFov.containsBlock(position));
            }).stream().map(e -> (Player) e).collect(Collectors.toSet());

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
                System.out.println(this.visible);
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

    private MutableBlockVolume getFieldOfView(Direction direction, double range) {
        Location<World> location = this.npc.getLocation();
        Vector3i position = location.getBlockPosition();
        Vector3i directionOffset = direction.asBlockOffset();
        Vector3i flippedOffset = new Vector3i(directionOffset.getZ(), directionOffset.getY(), directionOffset.getX());

        Vector3i min = position
            .sub(flippedOffset.mul(range))
            .sub(Vector3i.UNIT_Y.mul(range));

        Vector3i max = position
            .add(directionOffset.mul(range))
            .add(flippedOffset.mul(range))
            .add(Vector3i.UNIT_Y.mul(range));

        return location.getExtent().getBlockView(min, max);
    }

    private void renderFovPreview(MutableBlockVolume fov) {
        if (!this.preview) {
            fov.getBlockWorker(Cause.source(this.plugin.self).build()).iterate((v, x, y, z) ->
                fov.setBlock(
                    x, y, z,
                    BlockState.builder().blockType(BlockTypes.GLASS).build(),
                    Cause.source(this.plugin.self).build()));
            this.preview = true;
        }
    }

}
