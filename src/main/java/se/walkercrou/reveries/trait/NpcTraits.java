package se.walkercrou.reveries.trait;

public final class NpcTraits {

    public static final NpcTrait INVULNERABLE = new SimpleNpcTrait("invuln");

    public static final NpcTrait IMMOBILE = new SimpleNpcTrait("immobile");

    public static final NpcTrait NO_COLLIDE = new SimpleNpcTrait("nocollide");

    public static final NpcTrait NO_FIRE = new SimpleNpcTrait("nofire");

    public static final NpcTrait HEAD_TRACKING = new SimpleNpcTrait("headtracking");

    private NpcTraits() {}

}
