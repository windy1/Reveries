package se.walkercrou.peeps.property;

import static se.walkercrou.peeps.property.NpcProperties.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import org.spongepowered.api.registry.CatalogRegistryModule;
import org.spongepowered.api.registry.util.RegisterCatalog;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public final class NpcPropertyRegistryModule implements CatalogRegistryModule<NpcProperty> {

    public NpcPropertyRegistryModule() {
        registerDefaults();
    }

    @RegisterCatalog(NpcProperties.class)
    private final Map<String, NpcProperty> propertyMappings = Maps.newHashMap();

    @Override
    public Optional<NpcProperty> getById(String id) {
        return Optional.ofNullable(this.propertyMappings.get(id.toLowerCase()));
    }

    @Override
    public Collection<NpcProperty> getAll() {
        return ImmutableList.copyOf(this.propertyMappings.values());
    }

    @Override
    public void registerDefaults() {
        this.propertyMappings.put("displayName", DISPLAY_NAME);
        this.propertyMappings.put("skin", SKIN);
        this.propertyMappings.put("sightFront", FRONT_SIGHT_RANGE);
        this.propertyMappings.put("sightBack", BACK_SIGHT_RANGE);
        this.propertyMappings.put("location", LOCATION);
        this.propertyMappings.put("riding", RIDING);
        this.propertyMappings.put("rotation", ROTATION);
        this.propertyMappings.put("nameTag", NAME_TAG_VISIBLE);
    }

}
