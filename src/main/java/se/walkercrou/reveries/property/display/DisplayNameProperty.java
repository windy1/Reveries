package se.walkercrou.reveries.property.display;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.DisplayNameData;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;
import se.walkercrou.reveries.data.NpcKeys;
import se.walkercrou.reveries.data.npc.NpcData;
import se.walkercrou.reveries.property.NpcProperty;
import se.walkercrou.reveries.property.PropertyException;

import java.util.Optional;

import javax.annotation.Nullable;

public final class DisplayNameProperty implements NpcProperty<String> {

    @Override
    public boolean supports(Object value) {
        return value instanceof String;
    }

    @Override
    public boolean set(Living npc, String value, @Nullable CommandSource src) {
        Text displayName = TextSerializers.FORMATTING_CODE.deserialize(value);
        NpcData data = npc.get(NpcData.class).get();
        return npc.offer(data.set(NpcKeys.DISPLAY_NAME, displayName)).isSuccessful()
            && (!npc.supports(DisplayNameData.class)
                || npc.offer(npc.getOrCreate(DisplayNameData.class).get()
                    .set(Keys.DISPLAY_NAME, displayName)).isSuccessful());
    }

    @Override
    public boolean clear(Living npc, @Nullable CommandSource src) throws PropertyException {
        return set(npc, "", src);
    }

    @Override
    public Optional<String> get(Living npc) {
        return Optional.of(TextSerializers.FORMATTING_CODE.serialize(npc.get(NpcData.class).get().displayName().get()));
    }

    @Override
    public String getId() {
        return "displayName";
    }

    @Override
    public String getName() {
        return "Display Name";
    }

}
