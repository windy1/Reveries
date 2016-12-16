package se.walkercrou.peeps.data.impl.base;

import com.google.common.collect.Sets;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.OptionalValue;
import org.spongepowered.api.data.value.mutable.SetValue;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.text.Text;
import se.walkercrou.peeps.data.base.ImmutableNpcData;
import se.walkercrou.peeps.data.base.NpcData;
import se.walkercrou.peeps.data.NpcKeys;
import se.walkercrou.peeps.trait.NpcTrait;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public final class PeepsNpcData extends AbstractData<NpcData, ImmutableNpcData> implements NpcData {

    public static final int CONTENT_VERSION = 1;

    private UUID ownerId;
    private Text displayName;
    private Set<NpcTrait> traits;

    public PeepsNpcData(UUID ownerId, Text displayName, Set<NpcTrait> traits) {
        this.ownerId = ownerId;
        this.displayName = displayName;
        this.traits = traits;
        registerGettersAndSetters();
    }

    public PeepsNpcData() {
        this(null, null, Sets.newHashSet());
    }

    @Override
    public Value<UUID> ownerId() {
        return Sponge.getRegistry().getValueFactory().createValue(NpcKeys.OWNER_ID, this.ownerId);
    }

    @Override
    public OptionalValue<Text> displayName() {
        return Sponge.getRegistry().getValueFactory().createOptionalValue(NpcKeys.DISPLAY_NAME, this.displayName);
    }

    @Override
    public SetValue<NpcTrait> traits() {
        return Sponge.getRegistry().getValueFactory().createSetValue(NpcKeys.TRAITS, this.traits);
    }

    @Override
    protected void registerGettersAndSetters() {
        registerFieldGetter(NpcKeys.OWNER_ID, () -> this.ownerId);
        registerFieldSetter(NpcKeys.OWNER_ID, value -> this.ownerId = value);
        registerKeyValue(NpcKeys.OWNER_ID, this::ownerId);

        registerFieldGetter(NpcKeys.DISPLAY_NAME, () -> Optional.ofNullable(this.displayName));
        registerFieldSetter(NpcKeys.DISPLAY_NAME, value -> this.displayName = value.orElse(null));
        registerKeyValue(NpcKeys.DISPLAY_NAME, this::displayName);

        registerFieldGetter(NpcKeys.TRAITS, () -> this.traits);
        registerFieldSetter(NpcKeys.TRAITS, value -> this.traits = value);
        registerKeyValue(NpcKeys.TRAITS, this::traits);
    }

    @Override
    public Optional<NpcData> fill(DataHolder dataHolder, MergeFunction overlap) {
        return Optional.empty();
    }

    @Override
    public Optional<NpcData> from(DataContainer container) {
        if (!container.contains(NpcKeys.OWNER_ID, NpcKeys.DISPLAY_NAME, NpcKeys.TRAITS))
            return Optional.empty();
        this.ownerId = container.getObject(NpcKeys.OWNER_ID.getQuery(), UUID.class).get();
        this.displayName = container.getObject(NpcKeys.DISPLAY_NAME.getQuery(), Text.class).get();
        this.traits = Sets.newHashSet(container.getObjectList(NpcKeys.TRAITS.getQuery(), NpcTrait.class).get());
        return Optional.of(this);
    }

    @Override
    public PeepsNpcData copy() {
        return new PeepsNpcData(this.ownerId, this.displayName, this.traits);
    }

    @Override
    public PeepsImmutableNpcData asImmutable() {
        return new PeepsImmutableNpcData(this.ownerId, this.displayName, this.traits);
    }

    @Override
    public int getContentVersion() {
        return CONTENT_VERSION;
    }

    @Override
    public DataContainer toContainer() {
        return super.toContainer()
            .set(NpcKeys.OWNER_ID, this.ownerId)
            .set(NpcKeys.DISPLAY_NAME, Optional.ofNullable(this.displayName))
            .set(NpcKeys.TRAITS, this.traits);
    }

}
