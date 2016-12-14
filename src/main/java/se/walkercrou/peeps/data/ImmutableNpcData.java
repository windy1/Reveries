package se.walkercrou.peeps.data;

import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.value.immutable.ImmutableOptionalValue;
import org.spongepowered.api.data.value.immutable.ImmutableSetValue;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.text.Text;
import se.walkercrou.peeps.trait.NpcTrait;

import java.util.UUID;

public interface ImmutableNpcData extends ImmutableDataManipulator<ImmutableNpcData, NpcData> {

    ImmutableValue<UUID> ownerId();

    ImmutableOptionalValue<Text> displayName();

    ImmutableSetValue<NpcTrait> traits();

}
