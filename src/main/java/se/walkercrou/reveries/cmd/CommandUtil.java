package se.walkercrou.reveries.cmd;

import static se.walkercrou.reveries.Messages.MISSING_ENTITY;
import static se.walkercrou.reveries.Messages.NOT_AN_NPC;
import static se.walkercrou.reveries.Messages.NO_ENTITY_TARGET;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.extent.EntityUniverse;

import java.util.Optional;

public final class CommandUtil {

    private CommandUtil() {}

    public static Living getTargetedNpc(CommandSource src, Optional<Entity> arg) throws CommandException {
        Entity npc = getTargetedEntity(src, arg);
        if (!(npc instanceof Living))
            throw new CommandException(NOT_AN_NPC);
        return (Living) npc;
    }

    public static Entity getTargetedEntity(CommandSource src, Optional<Entity> arg) throws CommandException {
        Entity entity;
        if (!arg.isPresent()) {
            if (!(src instanceof Player))
                throw new CommandException(MISSING_ENTITY);
            Player player = (Player) src;
            entity = player.getWorld().getIntersectingEntities(player, 10).stream()
                .filter(e -> !e.getEntity().equals(player)).findFirst()
                .map(EntityUniverse.EntityHit::getEntity).orElse(null);
        } else
            entity = arg.get();
        if (entity == null)
            throw new CommandException(NO_ENTITY_TARGET);
        return entity;
    }

}
