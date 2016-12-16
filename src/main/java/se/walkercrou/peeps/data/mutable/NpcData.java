package se.walkercrou.peeps.data.mutable;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.value.mutable.SetValue;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.text.Text;
import se.walkercrou.peeps.data.immutable.ImmutableNpcData;
import se.walkercrou.peeps.trait.NpcTrait;

import java.util.UUID;

public interface NpcData extends DataManipulator<NpcData, ImmutableNpcData> {

    Value<UUID> ownerId();

    Value<Text> displayName();

    SetValue<NpcTrait> traits();

}
