package se.walkercrou.peeps.property.transform;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.Living;
import se.walkercrou.peeps.property.NpcProperty;
import se.walkercrou.peeps.property.PropertyException;

import java.util.Optional;

import javax.annotation.Nullable;

public final class RotationProperty implements NpcProperty<Vector3d> {

    @Override
    public boolean supports(Object value) {
        return value instanceof Vector3d;
    }

    @Override
    public boolean set(Living npc, Vector3d value, @Nullable CommandSource src) {
        npc.setRotation(value);
        npc.setHeadRotation(value);
        return true;
    }

    @Override
    public boolean clear(Living npc, @Nullable CommandSource src) throws PropertyException {
        return set(npc, Vector3d.ZERO, src);
    }

    @Override
    public Optional<Vector3d> get(Living npc) {
        return Optional.of(npc.getRotation());
    }

    @Override
    public String getId() {
        return "rotation";
    }

    @Override
    public String getName() {
        return "Rotation";
    }

}
