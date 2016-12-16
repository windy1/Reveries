package se.walkercrou.peeps.data.impl.immutable;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData;
import org.spongepowered.api.data.value.immutable.ImmutableSetValue;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.text.Text;
import se.walkercrou.peeps.data.NpcKeys;
import se.walkercrou.peeps.data.immutable.ImmutableNpcData;
import se.walkercrou.peeps.data.impl.mutable.PeepsNpcData;
import se.walkercrou.peeps.data.mutable.NpcData;
import se.walkercrou.peeps.trait.NpcTrait;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public final class PeepsImmutableNpcData extends AbstractImmutableData<ImmutableNpcData, NpcData> implements ImmutableNpcData {

    private final UUID ownerId;
    private final Text displayName;
    private final Set<NpcTrait> traits;

    public PeepsImmutableNpcData(UUID ownerId, Text displayName, Set<NpcTrait> traits) {
        this.ownerId = ownerId;
        this.displayName = displayName;
        this.traits = traits;
        registerGetters();
    }

    @Override
    public ImmutableValue<UUID> ownerId() {
        return Sponge.getRegistry().getValueFactory().createValue(NpcKeys.OWNER_ID, this.ownerId).asImmutable();
    }

    @Override
    public ImmutableValue<Text> displayName() {
        return Sponge.getRegistry().getValueFactory().createValue(NpcKeys.DISPLAY_NAME, this.displayName).asImmutable();
    }

    @Override
    public ImmutableSetValue<NpcTrait> traits() {
        return Sponge.getRegistry().getValueFactory().createSetValue(NpcKeys.TRAITS, this.traits).asImmutable();
    }

    @Override
    protected void registerGetters() {
        registerFieldGetter(NpcKeys.OWNER_ID, () -> this.ownerId);
        registerKeyValue(NpcKeys.OWNER_ID, this::ownerId);

        registerFieldGetter(NpcKeys.DISPLAY_NAME, () -> Optional.ofNullable(this.displayName));
        registerKeyValue(NpcKeys.DISPLAY_NAME, this::displayName);

        registerFieldGetter(NpcKeys.TRAITS, () -> this.traits);
        registerKeyValue(NpcKeys.TRAITS, this::traits);
    }

    @Override
    public PeepsNpcData asMutable() {
        return new PeepsNpcData(this.ownerId, this.displayName, this.traits);
    }

    @Override
    public int getContentVersion() {
        return PeepsNpcData.CONTENT_VERSION;
    }

    @Override
    public DataContainer toContainer() {
        return super.toContainer()
            .set(NpcKeys.OWNER_ID, this.ownerId)
            .set(NpcKeys.DISPLAY_NAME, this.displayName)
            .set(NpcKeys.TRAITS, this.traits);
    }

}
