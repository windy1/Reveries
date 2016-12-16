package se.walkercrou.peeps.data.impl.sight;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import se.walkercrou.peeps.data.sight.ImmutableSightData;
import se.walkercrou.peeps.data.NpcKeys;
import se.walkercrou.peeps.data.sight.SightData;

public final class PeepsImmutableSightData extends AbstractImmutableData<ImmutableSightData, SightData> implements ImmutableSightData {

    private final double frontSightRange;
    private final double backSightRange;

    public PeepsImmutableSightData(double frontSightRange, double backSightRange) {
        this.frontSightRange = frontSightRange;
        this.backSightRange = backSightRange;
        registerGetters();
    }

    @Override
    public ImmutableValue<Double> frontSightRange() {
        return Sponge.getRegistry().getValueFactory()
            .createValue(NpcKeys.FRONT_SIGHT_RANGE, this.frontSightRange).asImmutable();
    }

    @Override
    public ImmutableValue<Double> backSightRange() {
        return Sponge.getRegistry().getValueFactory()
            .createValue(NpcKeys.BACK_SIGHT_RANGE, this.backSightRange).asImmutable();
    }

    @Override
    protected void registerGetters() {
        registerFieldGetter(NpcKeys.FRONT_SIGHT_RANGE, () -> this.frontSightRange);
        registerKeyValue(NpcKeys.FRONT_SIGHT_RANGE, this::frontSightRange);

        registerFieldGetter(NpcKeys.BACK_SIGHT_RANGE, () -> this.backSightRange);
        registerKeyValue(NpcKeys.BACK_SIGHT_RANGE, this::backSightRange);
    }

    @Override
    public SightData asMutable() {
        return new PeepsSightData(this.frontSightRange, this.backSightRange);
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Override
    public DataContainer toContainer() {
        return super.toContainer()
            .set(NpcKeys.FRONT_SIGHT_RANGE, this.frontSightRange)
            .set(NpcKeys.BACK_SIGHT_RANGE, this.backSightRange);
    }

}
