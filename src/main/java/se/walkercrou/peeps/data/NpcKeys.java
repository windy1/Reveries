package se.walkercrou.peeps.data;

import static org.spongepowered.api.data.key.KeyFactory.makeSingleKey;

import com.google.common.reflect.TypeToken;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.OptionalValue;
import org.spongepowered.api.data.value.mutable.SetValue;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.text.Text;
import se.walkercrou.peeps.trait.NpcTrait;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public final class NpcKeys {

    public static final Key<Value<UUID>> OWNER_ID = makeSingleKey(TypeToken.of(UUID.class),
        new TypeToken<Value<UUID>>() {}, DataQuery.of("OwnerId"), "peeps:ownerId", "Owner ID");

    public static final Key<SetValue<NpcTrait>> TRAITS = makeSingleKey(new TypeToken<Set<NpcTrait>>() {},
        new TypeToken<SetValue<NpcTrait>>() {}, DataQuery.of("Traits"), "peeps:traits", "Traits");

    public static final Key<OptionalValue<Text>> DISPLAY_NAME = makeSingleKey(new TypeToken<Optional<Text>>() {},
        new TypeToken<OptionalValue<Text>>() {}, DataQuery.of("DisplayName"), "peeps:display_name", "Display Name");

    private NpcKeys() {}

}
