package se.walkercrou.peeps;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.collect.Sets;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.MutableBlockVolume;
import se.walkercrou.peeps.data.sight.SightData;
import se.walkercrou.peeps.event.npc.NpcLoseSightOfPlayerEvent;
import se.walkercrou.peeps.event.npc.NpcSpotPlayerEvent;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class NpcMonitor implements Consumer<Task> {

    private final Peeps plugin;
    private final Living npc;
    private final Set<Player> previouslyVisible = Sets.newHashSet();
    private Living tracking;

    boolean preview = false;

    public NpcMonitor(Peeps plugin, Living npc) {
        this.plugin = plugin;
        this.npc = npc;
    }

    public void setTracking(Living tracking) {
        this.tracking = tracking;
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

            Set<Player> visibleResult = Sets.newHashSet();
            for (Player player : visiblePlayers) {
                if (!this.previouslyVisible.contains(player)) {
                    boolean cancelled = this.plugin.game.getEventManager().post(new NpcSpotPlayerEvent(
                        this.npc, Cause.source(this.plugin.self).owner(player).build()));
                    if (!cancelled)
                        visibleResult.add(player);
                }
            }

            for (Player player : this.previouslyVisible) {
                if (!visiblePlayers.contains(player)) {
                    boolean cancelled = this.plugin.game.getEventManager().post(new NpcLoseSightOfPlayerEvent(
                        this.npc, Cause.source(this.plugin.self).owner(player).build()));
                    if (cancelled)
                        visibleResult.add(player);
                }
            }

            this.previouslyVisible.clear();
            this.previouslyVisible.addAll(visibleResult);
        }
    }

    private MutableBlockVolume getFieldOfView(Direction direction, double range) {
        System.out.println("FOV <" + direction + "> : " + range);
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
