package se.walkercrou.reveries.data.impl;

import static com.google.common.collect.Sets.newHashSet;

import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.text.Text;
import se.walkercrou.reveries.data.npc.ImmutableNpcData;
import se.walkercrou.reveries.data.npc.NpcData;
import se.walkercrou.reveries.data.NpcKeys;
import se.walkercrou.reveries.trait.NpcTrait;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public final class NpcDataManipulatorBuilder implements DataManipulatorBuilder<NpcData, ImmutableNpcData> {

    @Override
    public NpcData create() {
        return new ReveriesNpcData();
    }

    @Override
    public Optional<NpcData> createFrom(DataHolder dataHolder) {
        return Optional.of(dataHolder.get(NpcData.class).orElse(new ReveriesNpcData()));
    }

    @Override
    public Optional<NpcData> build(DataView container) throws InvalidDataException {
        if (!container.contains(NpcKeys.OWNER_ID, Keys.DISPLAY_NAME, NpcKeys.SIGHT_RANGE, NpcKeys.TRAITS))
            return Optional.empty();
        UUID ownerId = container.getObject(NpcKeys.OWNER_ID.getQuery(), UUID.class).get();
        Text displayName = container.getSerializable(NpcKeys.DISPLAY_NAME.getQuery(), Text.class).get();
        double sightRange = container.getDouble(NpcKeys.SIGHT_RANGE.getQuery()).get();
        Set<NpcTrait> traits = newHashSet(
            container.getCatalogTypeList(NpcKeys.TRAITS.getQuery(), NpcTrait.class).get());
        return Optional.of(new ReveriesNpcData(ownerId, displayName, sightRange, traits));
    }

}
