package se.walkercrou.reveries.data.npc;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.value.mutable.SetValue;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.text.Text;
import se.walkercrou.reveries.trait.NpcTrait;

import java.util.UUID;

public interface NpcData extends DataManipulator<NpcData, ImmutableNpcData> {

    Value<UUID> ownerId();

    Value<Text> displayName();

    Value<Double> sightRange();

    SetValue<NpcTrait> traits();

}
