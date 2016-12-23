package se.walkercrou.reveries.event.npc;

import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.entity.living.TargetLivingEvent;
import org.spongepowered.api.event.impl.AbstractEvent;

public final class NpcLoseSightOfPlayerEvent extends AbstractEvent implements TargetLivingEvent, Cancellable {

    private final Living npc;
    private final Cause cause;

    private boolean cancelled;

    public NpcLoseSightOfPlayerEvent(Living npc, Cause cause) {
        this.npc = npc;
        this.cause = cause;
    }

    @Override
    public Living getTargetEntity() {
        return this.npc;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public Cause getCause() {
        return this.cause;
    }

}
