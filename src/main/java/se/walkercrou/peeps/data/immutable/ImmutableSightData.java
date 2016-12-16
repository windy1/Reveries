package se.walkercrou.peeps.data.immutable;

import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import se.walkercrou.peeps.data.mutable.SightData;

public interface ImmutableSightData extends ImmutableDataManipulator<ImmutableSightData, SightData> {

    ImmutableValue<Double> frontSightRange();

    ImmutableValue<Double> backSightRange();

}
