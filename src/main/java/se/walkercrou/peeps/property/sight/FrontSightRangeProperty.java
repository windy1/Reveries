package se.walkercrou.peeps.property.sight;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.Living;
import se.walkercrou.peeps.Peeps;
import se.walkercrou.peeps.data.NpcKeys;
import se.walkercrou.peeps.data.impl.sight.PeepsSightData;
import se.walkercrou.peeps.data.sight.SightData;
import se.walkercrou.peeps.property.NpcProperty;
import se.walkercrou.peeps.property.PropertyException;

import java.util.Optional;

public final class FrontSightRangeProperty implements NpcProperty<Double> {

    @Override
    public boolean supports(Object value) {
        return value instanceof Double;
    }

    @Override
    public boolean set(Living npc, Double value, CommandSource src) throws PropertyException {
        System.out.println("value = " + value);
        Optional<SightData> data = npc.get(SightData.class);
        boolean result = data.map(sd -> npc.offer(sd.set(NpcKeys.FRONT_SIGHT_RANGE, value)).isSuccessful())
            .orElseGet(() -> npc.offer(new PeepsSightData(value, 0)).isSuccessful());
        Peeps plugin = Peeps.INSTANCE;
        if (result && !plugin.isMonitoring(npc))
            plugin.monitor(npc);
        return result;
    }

    @Override
    public String getId() {
        return "sightFront";
    }

    @Override
    public String getName() {
        return "Front Sight Range";
    }

}
