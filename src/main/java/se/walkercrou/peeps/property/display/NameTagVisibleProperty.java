package se.walkercrou.peeps.property.display;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.entity.CustomNameVisibleData;
import org.spongepowered.api.entity.living.Living;
import se.walkercrou.peeps.property.NpcProperty;
import se.walkercrou.peeps.property.PropertyException;

import javax.annotation.Nullable;

public final class NameTagVisibleProperty implements NpcProperty<Boolean> {

    @Override
    public boolean supports(Object value) {
        return value instanceof Boolean;
    }

    @Override
    public boolean set(Living npc, Boolean value, @Nullable CommandSource src) throws PropertyException {
        return npc.supports(CustomNameVisibleData.class)
            && npc.offer(npc.get(CustomNameVisibleData.class).get()
                .set(Keys.CUSTOM_NAME_VISIBLE, value)).isSuccessful();
    }

    @Override
    public String getId() {
        return "nameTag";
    }

    @Override
    public String getName() {
        return "Name Tag";
    }

}
