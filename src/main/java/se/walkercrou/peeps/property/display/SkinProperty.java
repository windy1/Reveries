package se.walkercrou.peeps.property.display;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.entity.SkinData;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.text.Text;
import se.walkercrou.peeps.Messages;
import se.walkercrou.peeps.property.NpcProperty;
import se.walkercrou.peeps.property.PropertyException;

import java.util.UUID;

public final class SkinProperty implements NpcProperty<String> {

    @Override
    public boolean supports(Object value) {
        return value instanceof String;
    }

    @Override
    public boolean set(Living npc, String value, CommandSource src) throws PropertyException {
        if (!npc.supports(SkinData.class))
            throw new PropertyException(Messages.IMMUTABLE_SKIN);

        SkinData data = npc.getOrCreate(SkinData.class).get();
        try {
            UUID skinId = UUID.fromString(value);
            return npc.offer(data.set(Keys.SKIN_UNIQUE_ID, skinId)).isSuccessful();
        } catch (IllegalArgumentException e) {
            if (src != null)
                src.sendMessage(Messages.SKIN_LOOKUP);

            Sponge.getGame().getServer().getGameProfileManager().get(value).whenComplete((profile, thrown) -> {
                if (thrown != null) {
                    if (src != null)
                        src.sendMessage(Messages.SKIN_NOT_FOUND);
                    return;
                }

                boolean result = npc.offer(data.set(Keys.SKIN_UNIQUE_ID, profile.getUniqueId())).isSuccessful();
                if (src != null && result)
                    src.sendMessage(Messages.UPDATED_PROPS, ImmutableMap.of("amount", Text.of(1)));
            });

            return false;
        }
    }

    @Override
    public String getId() {
        return "skin";
    }

    @Override
    public String getName() {
        return "Skin";
    }

}
