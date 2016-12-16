package se.walkercrou.peeps.property;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import se.walkercrou.peeps.property.display.DisplayNameProperty;
import se.walkercrou.peeps.property.display.SkinProperty;
import se.walkercrou.peeps.property.sight.BackSightRangeProperty;
import se.walkercrou.peeps.property.sight.FrontSightRangeProperty;
import se.walkercrou.peeps.property.transform.LocationProperty;
import se.walkercrou.peeps.property.transform.RidingProperty;
import se.walkercrou.peeps.property.transform.RotationProperty;

public final class NpcProperties {

    public static final NpcProperty<String> DISPLAY_NAME = new DisplayNameProperty();

    public static final NpcProperty<String> SKIN = new SkinProperty();

    public static final NpcProperty<Double> FRONT_SIGHT_RANGE = new FrontSightRangeProperty();

    public static final NpcProperty<Double> BACK_SIGHT_RANGE = new BackSightRangeProperty();

    public static final NpcProperty<Location<World>> LOCATION = new LocationProperty();

    public static final NpcProperty<Entity> RIDING = new RidingProperty();

    public static final NpcProperty<Vector3d> ROTATION = new RotationProperty();

    private NpcProperties() {}

}
