package se.walkercrou.peeps.trait;

import org.spongepowered.api.data.DataQuery;

public final class SimpleNpcTrait implements NpcTrait {

    private static final DataQuery QUERY_ID = DataQuery.of("Id");

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
