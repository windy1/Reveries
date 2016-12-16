package se.walkercrou.peeps.property.display;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.DisplayNameData;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;
import se.walkercrou.peeps.data.NpcKeys;
import se.walkercrou.peeps.data.mutable.NpcData;
import se.walkercrou.peeps.property.NpcProperty;

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
    public String getId() {
        return "displayName";
    }

    @Override
    public String getName() {
        return "Display Name";
    }

}
