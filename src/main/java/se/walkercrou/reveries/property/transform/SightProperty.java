package se.walkercrou.reveries.property.transform;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.Living;
import se.walkercrou.reveries.Reveries;
import se.walkercrou.reveries.data.NpcKeys;
import se.walkercrou.reveries.data.npc.NpcData;
import se.walkercrou.reveries.property.NpcProperty;
import se.walkercrou.reveries.property.PropertyException;

import java.util.Optional;

import javax.annotation.Nullable;

public final class SightProperty implements NpcProperty<Double> {

    @Override
    public boolean supports(Object value) {
        return value instanceof Double;
    }

    @Override
    public boolean set(Living npc, Double value, @Nullable CommandSource src) throws PropertyException {
        boolean success = npc.offer(npc.get(NpcData.class).get().set(NpcKeys.SIGHT_RANGE, value)).isSuccessful();
        if (value > 0 && success && !Reveries.INSTANCE.isMonitoring(npc))
            Reveries.INSTANCE.monitor(npc);
        return success;
    }

    @Override public boolean clear(Living npc, @Nullable CommandSource src) throws PropertyException {
        return set(npc, 0.0, src);
    }

    @Override
    public Optional<Double> get(Living npc) {
        return Optional.of(npc.get(NpcData.class).get().sightRange().get());
    }

    @Override
    public String getId() {
        return "sight";
    }

    @Override
    public String getName() {
        return "Sight Range";
    }

}
