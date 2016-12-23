package se.walkercrou.peeps.event;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.CollideEntityEvent;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.entity.IgniteEntityEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.Getter;
import se.walkercrou.peeps.data.npc.NpcData;
import se.walkercrou.peeps.trait.NpcTrait;
import se.walkercrou.peeps.trait.NpcTraits;

import java.util.List;

public final class EntityListener {

    @Listener
    public void onNpcDamage(DamageEntityEvent event, @Getter("getTargetEntity") Entity entity) {
        checkTrait(event, entity, NpcTraits.INVULNERABLE);
    }

    @Listener
    public void onNpcMove(MoveEntityEvent event, @Getter("getTargetEntity") Entity entity) {
        checkTrait(event, entity, NpcTraits.IMMOBILE);
    }

    @Listener
    public void onNpcCollide(CollideEntityEvent event, @Getter("getEntities") List<Entity> entities) {
        entities.forEach(e -> checkTrait(event, e, NpcTraits.NO_COLLIDE));
    }

    @Listener
    public void onNpcIgnite(IgniteEntityEvent event, @Getter("getTargetEntity") Entity entity) {
        checkTrait(event, entity, NpcTraits.NO_FIRE);
    }

    private void checkTrait(Cancellable event, Entity entity, NpcTrait trait) {
        entity.get(NpcData.class).ifPresent(npcData -> {
            if (npcData.traits().get().contains(trait))
                event.setCancelled(true);
        });
    }

}
