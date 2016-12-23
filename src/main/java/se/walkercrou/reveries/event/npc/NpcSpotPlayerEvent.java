package se.walkercrou.reveries.event.npc;

import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.entity.living.TargetLivingEvent;
import org.spongepowered.api.event.impl.AbstractEvent;

public final class NpcSpotPlayerEvent extends AbstractEvent implements TargetLivingEvent, Cancellable {

    private final Cause cause;
    private final Living npc;

    private boolean cancelled;

    public NpcSpotPlayerEvent(Living npc, Cause cause) {
        this.cause = cause;
        this.npc = npc;
    }

    @Override
    public Living getTargetEntity() {
        return this.npc;
    }

    @Override
    public Cause getCause() {
        return this.cause;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

}
