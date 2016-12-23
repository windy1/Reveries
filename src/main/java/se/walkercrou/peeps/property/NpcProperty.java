package se.walkercrou.peeps.property;

import org.spongepowered.api.CatalogType;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.util.annotation.CatalogedBy;

import java.util.Optional;

import javax.annotation.Nullable;

@CatalogedBy(NpcProperties.class)
public interface NpcProperty<T> extends CatalogType {

    boolean supports(Object value);

    boolean set(Living npc, T value, @Nullable CommandSource src) throws PropertyException;

    Optional<T> get(Living npc);

}
