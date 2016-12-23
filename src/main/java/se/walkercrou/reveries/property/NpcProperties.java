package se.walkercrou.reveries.property;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import se.walkercrou.reveries.property.display.DisplayNameProperty;
import se.walkercrou.reveries.property.display.NameTagVisibleProperty;
import se.walkercrou.reveries.property.display.SkinProperty;
import se.walkercrou.reveries.property.transform.LocationProperty;
import se.walkercrou.reveries.property.transform.RidingProperty;
import se.walkercrou.reveries.property.transform.RotationProperty;
import se.walkercrou.reveries.property.transform.SightProperty;

public final class NpcProperties {

    public static final NpcProperty<String> DISPLAY_NAME = new DisplayNameProperty();

    public static final NpcProperty<String> SKIN = new SkinProperty();

    public static final NpcProperty<Double> SIGHT_RANGE = new SightProperty();

    public static final NpcProperty<Location<World>> LOCATION = new LocationProperty();

    public static final NpcProperty<Entity> RIDING = new RidingProperty();

    public static final NpcProperty<Vector3d> ROTATION = new RotationProperty();

    public static final NpcProperty<Boolean> NAME_TAG_VISIBLE = new NameTagVisibleProperty();

    private NpcProperties() {}

}
