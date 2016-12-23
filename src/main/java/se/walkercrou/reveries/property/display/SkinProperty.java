package se.walkercrou.reveries.property.display;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.entity.SkinData;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.text.Text;
import se.walkercrou.reveries.Messages;
import se.walkercrou.reveries.property.NpcProperty;
import se.walkercrou.reveries.property.PropertyException;

import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

public final class SkinProperty implements NpcProperty<String> {

    private static final UUID UUID_EMPTY = UUID.fromString("00000000-0000-0000-0000-000000000000");

    @Override
    public boolean supports(Object value) {
        return value instanceof String;
    }

    @Override
    public boolean set(Living npc, String value, CommandSource src) throws PropertyException {
        SkinData data = checkNpc(npc).getOrCreate(SkinData.class).get();
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
    public boolean clear(Living npc, @Nullable CommandSource src) throws PropertyException {
        return checkNpc(npc).offer(npc.getOrCreate(SkinData.class).get()
            .set(Keys.SKIN_UNIQUE_ID, UUID_EMPTY)).isSuccessful();
    }

    private Living checkNpc(Living npc) throws PropertyException {
        if (!npc.supports(SkinData.class))
            throw new PropertyException(Messages.IMMUTABLE_SKIN);
        return npc;
    }

    @Override
    public Optional<String> get(Living npc) {
        if (!npc.supports(SkinData.class))
            return Optional.empty();
        return Optional.of(npc.getOrCreate(SkinData.class).get().skinUniqueId().get().toString());
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
