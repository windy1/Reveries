package se.walkercrou.peeps.data.impl.sight;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.Value;
import se.walkercrou.peeps.data.sight.ImmutableSightData;
import se.walkercrou.peeps.data.NpcKeys;
import se.walkercrou.peeps.data.sight.SightData;

import java.util.Optional;

public class PeepsSightData extends AbstractData<SightData, ImmutableSightData> implements SightData {

    private double frontSightRange;
    private double backSightRange;

    public PeepsSightData(double frontSightRange, double backSightRange) {
        this.frontSightRange = frontSightRange;
        this.backSightRange = backSightRange;
        registerGettersAndSetters();
    }

    public PeepsSightData() {
        this(0, 0);
    }

    @Override
    public Value<Double> frontSightRange() {
        return Sponge.getRegistry().getValueFactory().createValue(NpcKeys.FRONT_SIGHT_RANGE, this.frontSightRange);
    }

    @Override
    public Value<Double> backSightRange() {
        return Sponge.getRegistry().getValueFactory().createValue(NpcKeys.BACK_SIGHT_RANGE, this.backSightRange);
    }

    @Override
    protected void registerGettersAndSetters() {
        registerFieldGetter(NpcKeys.FRONT_SIGHT_RANGE, () -> this.frontSightRange);
        registerFieldSetter(NpcKeys.FRONT_SIGHT_RANGE, value -> this.frontSightRange = value);
        registerKeyValue(NpcKeys.FRONT_SIGHT_RANGE, this::frontSightRange);

        registerFieldGetter(NpcKeys.BACK_SIGHT_RANGE, () -> this.backSightRange);
        registerFieldSetter(NpcKeys.BACK_SIGHT_RANGE, value -> this.backSightRange = value);
        registerKeyValue(NpcKeys.BACK_SIGHT_RANGE, this::backSightRange);
    }

    @Override
    public Optional<SightData> fill(DataHolder dataHolder, MergeFunction overlap) {
        return Optional.empty();
    }

    @Override
    public Optional<SightData> from(DataContainer container) {
        if (!container.contains(NpcKeys.FRONT_SIGHT_RANGE, NpcKeys.BACK_SIGHT_RANGE))
            return Optional.empty();
        this.frontSightRange = container.getDouble(NpcKeys.FRONT_SIGHT_RANGE.getQuery()).get();
        this.backSightRange = container.getDouble(NpcKeys.BACK_SIGHT_RANGE.getQuery()).get();
        return Optional.of(this);
    }

    @Override
    public SightData copy() {
        return new PeepsSightData(this.frontSightRange, this.backSightRange);
    }

    @Override
    public ImmutableSightData asImmutable() {
        return new PeepsImmutableSightData(this.frontSightRange, this.backSightRange);
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
