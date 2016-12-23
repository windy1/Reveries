package se.walkercrou.peeps.data;

import static org.spongepowered.api.data.key.KeyFactory.makeSingleKey;

import com.google.common.reflect.TypeToken;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.SetValue;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.text.Text;
import se.walkercrou.peeps.trait.NpcTrait;

import java.util.Set;
import java.util.UUID;

public final class NpcKeys {

    public static final Key<Value<UUID>> OWNER_ID = makeSingleKey(TypeToken.of(UUID.class),
        new TypeToken<Value<UUID>>() {}, DataQuery.of("OwnerId"), "peeps:ownerId", "Owner ID");

    public static final Key<SetValue<NpcTrait>> TRAITS = makeSingleKey(new TypeToken<Set<NpcTrait>>() {},
        new TypeToken<SetValue<NpcTrait>>() {}, DataQuery.of("Traits"), "peeps:traits", "Traits");

    public static final Key<Value<Text>> DISPLAY_NAME = makeSingleKey(new TypeToken<Text>() {},
        new TypeToken<Value<Text>>() {}, DataQuery.of("DisplayName"), "peeps:display_name", "Display Name");

    public static final Key<Value<Double>> SIGHT_RANGE = makeSingleKey(TypeToken.of(Double.class),
        new TypeToken<Value<Double>>() {}, DataQuery.of("SightRange"), "peeps:sight_range",
        "Sight Range");

    private NpcKeys() {}

}
