package se.walkercrou.peeps.trait;

public final class SimpleNpcTrait implements NpcTrait {

    private final String id;

    public SimpleNpcTrait(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.id;
    }

}
