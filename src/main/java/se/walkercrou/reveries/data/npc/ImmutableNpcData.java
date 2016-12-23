package se.walkercrou.reveries.data.npc;

import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.value.immutable.ImmutableSetValue;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.text.Text;
import se.walkercrou.reveries.trait.NpcTrait;

import java.util.UUID;

public interface ImmutableNpcData extends ImmutableDataManipulator<ImmutableNpcData, NpcData> {

    ImmutableValue<UUID> ownerId();

    ImmutableValue<Text> displayName();

    ImmutableValue<Double> sightRange();

    ImmutableSetValue<NpcTrait> traits();

}
