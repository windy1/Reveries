package se.walkercrou.peeps.data.sight;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.value.mutable.Value;

public interface SightData extends DataManipulator<SightData, ImmutableSightData> {

    Value<Double> frontSightRange();

    Value<Double> backSightRange();

}
