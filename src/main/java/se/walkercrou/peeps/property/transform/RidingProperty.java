package se.walkercrou.peeps.property.transform;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;
import se.walkercrou.peeps.property.NpcProperty;

public final class RidingProperty implements NpcProperty<Entity> {

    @Override
    public boolean supports(Object value) {
        return value instanceof Entity;
    }

    @Override
    public boolean set(Living npc, Entity value, CommandSource src) {
        return npc.setVehicle(value).isSuccessful();
    }

    @Override
    public String getId() {
        return "riding";
    }

    @Override
    public String getName() {
        return "Riding";
    }

}
