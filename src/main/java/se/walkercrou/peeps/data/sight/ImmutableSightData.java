package se.walkercrou.peeps.data.sight;

import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

public interface ImmutableSightData extends ImmutableDataManipulator<ImmutableSightData, SightData> {

    ImmutableValue<Double> frontSightRange();

    ImmutableValue<Double> backSightRange();

}
