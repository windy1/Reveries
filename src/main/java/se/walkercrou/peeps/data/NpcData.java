package se.walkercrou.peeps.data;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.value.mutable.OptionalValue;
import org.spongepowered.api.data.value.mutable.SetValue;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.text.Text;
import se.walkercrou.peeps.trait.NpcTrait;

import java.util.UUID;

public interface NpcData extends DataManipulator<NpcData, ImmutableNpcData> {

    Value<UUID> ownerId();

    OptionalValue<Text> displayName();

    SetValue<NpcTrait> traits();

}
