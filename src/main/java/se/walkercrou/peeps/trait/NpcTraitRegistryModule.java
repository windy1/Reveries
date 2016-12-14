package se.walkercrou.peeps.trait;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.registry.CatalogRegistryModule;
import org.spongepowered.api.registry.util.RegisterCatalog;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public final class NpcTraitRegistryModule implements CatalogRegistryModule<NpcTrait> {

    @RegisterCatalog(NpcTraits.class)
    private final Map<String, NpcTrait> traitMappings = ImmutableMap.of(
        "invuln", NpcTraits.INVULNERABLE,
        "immobile", NpcTraits.IMMOBILE,
        "nocollide", NpcTraits.NO_COLLIDE,
        "nofire", NpcTraits.NO_FIRE);

    @Override
    public Optional<NpcTrait> getById(String id) {
        return Optional.ofNullable(this.traitMappings.get(id.toLowerCase()));
    }

    @Override
    public Collection<NpcTrait> getAll() {
        return ImmutableList.copyOf(this.traitMappings.values());
    }

}
