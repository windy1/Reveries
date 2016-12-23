package se.walkercrou.reveries.property.transform;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import se.walkercrou.reveries.property.NpcProperty;
import se.walkercrou.reveries.property.PropertyException;

import java.util.Optional;

import javax.annotation.Nullable;

public final class LocationProperty implements NpcProperty<Location<World>> {

    @Override
    public boolean supports(Object value) {
        return value instanceof Location && ((Location) value).getExtent() instanceof World;
    }

    @Override
    public boolean set(Living npc, Location<World> value, CommandSource src) {
        return npc.setLocation(value);
    }

    @Override
    public boolean clear(Living npc, @Nullable CommandSource src) throws PropertyException {
        return false;
    }

    @Override
    public Optional<Location<World>> get(Living npc) {
        return Optional.of(npc.getLocation());
    }

    @Override
    public String getId() {
        return "location";
    }

    @Override
    public String getName() {
        return "Location";
    }

}
