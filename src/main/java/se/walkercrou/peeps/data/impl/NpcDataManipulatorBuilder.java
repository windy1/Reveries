package se.walkercrou.peeps.data.impl;

import static com.google.common.collect.Sets.newHashSet;

import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.text.Text;
import se.walkercrou.peeps.data.ImmutableNpcData;
import se.walkercrou.peeps.data.NpcData;
import se.walkercrou.peeps.data.NpcKeys;
import se.walkercrou.peeps.trait.NpcTrait;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class NpcDataManipulatorBuilder implements DataManipulatorBuilder<NpcData, ImmutableNpcData> {

    @Override
    public NpcData create() {
        return new PeepsNpcData();
    }

    @Override
    public Optional<NpcData> createFrom(DataHolder dataHolder) {
        return Optional.of(dataHolder.get(NpcData.class).orElse(new PeepsNpcData()));
    }

    @Override
    public Optional<NpcData> build(DataView container) throws InvalidDataException {
        if (!container.contains(NpcKeys.OWNER_ID, Keys.DISPLAY_NAME, NpcKeys.TRAITS))
            return Optional.empty();
        UUID ownerId = container.getObject(NpcKeys.OWNER_ID.getQuery(), UUID.class).get();
        Text displayName = container.getObject(Keys.DISPLAY_NAME.getQuery(), Text.class).get();
        Set<NpcTrait> traits = newHashSet(container.getObjectList(NpcKeys.TRAITS.getQuery(), NpcTrait.class).get());
        return Optional.of(new PeepsNpcData(ownerId, displayName, traits));
    }

}
