package se.walkercrou.peeps.data.mutable;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.value.mutable.Value;
import se.walkercrou.peeps.data.immutable.ImmutableSightData;

public interface SightData extends DataManipulator<SightData, ImmutableSightData> {

    Value<Double> frontSightRange();

    Value<Double> backSightRange();

}
