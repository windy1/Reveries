package se.walkercrou.peeps.trait;

public final class NpcTraits {

    public static final NpcTrait INVULNERABLE = new SimpleNpcTrait("invuln");

    public static final NpcTrait IMMOBILE = new SimpleNpcTrait("immobile");

    public static final NpcTrait NO_COLLIDE = new SimpleNpcTrait("nocollide");

    public static final NpcTrait NO_FIRE = new SimpleNpcTrait("nofire");

    private NpcTraits() {}

}
