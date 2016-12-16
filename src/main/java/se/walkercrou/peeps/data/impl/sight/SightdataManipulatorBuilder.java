package se.walkercrou.peeps.data.impl.sight;

import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import se.walkercrou.peeps.data.sight.ImmutableSightData;
import se.walkercrou.peeps.data.NpcKeys;
import se.walkercrou.peeps.data.sight.SightData;

import java.util.Optional;

public class SightdataManipulatorBuilder implements DataManipulatorBuilder<SightData, ImmutableSightData> {

    @Override
    public SightData create() {
        return new PeepsSightData();
    }

    @Override
    public Optional<SightData> createFrom(DataHolder dataHolder) {
        return Optional.of(dataHolder.get(SightData.class).orElse(new PeepsSightData()));
    }

    @Override
    public Optional<SightData> build(DataView container) throws InvalidDataException {
        if (!container.contains(NpcKeys.FRONT_SIGHT_RANGE, NpcKeys.BACK_SIGHT_RANGE))
            return Optional.empty();
        double frontSightRange = container.getDouble(NpcKeys.FRONT_SIGHT_RANGE.getQuery()).get();
        double backSightRange = container.getDouble(NpcKeys.BACK_SIGHT_RANGE.getQuery()).get();
        return Optional.of(new PeepsSightData(frontSightRange, backSightRange));
    }

}
