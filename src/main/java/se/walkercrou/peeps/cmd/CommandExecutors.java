package se.walkercrou.peeps.cmd;

import static se.walkercrou.peeps.Messages.MISSING_ENTITY;
import static se.walkercrou.peeps.Messages.NOT_AN_NPC;
import static se.walkercrou.peeps.Messages.NO_ENTITY_TARGET;
import static se.walkercrou.peeps.Messages.NO_LOCATION;
import static se.walkercrou.peeps.Messages.SPAWN_FAILED;
import static se.walkercrou.peeps.Messages.SPAWN_SUCCESS;
import static se.walkercrou.peeps.Messages.UNSUPPORTED_PROP_TYPE;
import static se.walkercrou.peeps.Messages.UPDATED_PROPS;
import static se.walkercrou.peeps.Messages.UPDATED_TRAITS;
import static se.walkercrou.peeps.Messages.VERSION;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.EntityUniverse;
import se.walkercrou.peeps.Messages;
import se.walkercrou.peeps.NpcSpawnException;
import se.walkercrou.peeps.Peeps;
import se.walkercrou.peeps.data.NpcKeys;
import se.walkercrou.peeps.data.mutable.NpcData;
import se.walkercrou.peeps.property.NpcProperty;
import se.walkercrou.peeps.property.PropertyException;
import se.walkercrou.peeps.trait.NpcTrait;

import java.util.Optional;
import java.util.Set;

public final class CommandExecutors {

    private final Peeps plugin;

    public CommandExecutors(Peeps plugin) {
        this.plugin = plugin;
    }

    public CommandResult showVersion(CommandSource src, CommandContext context) {
        PluginContainer container = this.plugin.self;
        src.sendMessage(VERSION, ImmutableMap.of(
            "plugin.name", Text.of(container.getName()),
            "plugin.version", Text.of(container.getVersion().get())));
        return CommandResult.success();
    }

    public CommandResult createNpc(CommandSource src, CommandContext context) throws CommandException {
        EntityType entityType = context.<EntityType>getOne("entityType").orElse(EntityTypes.HUMAN);
        Optional<Location<World>> loc = context.getOne("location");
        if (!loc.isPresent() && !(src instanceof Player))
            throw new CommandException(NO_LOCATION);
        Location<World> location = loc.orElse(((Player) src).getLocation());
        try {
            Living npc = this.plugin.createAndSpawn(entityType, location, Cause.source(this.plugin).owner(src).build());
            NpcData npcData = npc.get(NpcData.class).get();
            src.sendMessage(SPAWN_SUCCESS);
            Messages.getNpcInfo(npc, npcData).sendTo(src);
            return CommandResult.success();
        } catch (NpcSpawnException e) {
            throw new CommandException(SPAWN_FAILED);
        }
    }

    public CommandResult getNpcInfo(CommandSource src, CommandContext context) throws CommandException {
        Living npc = getTargetedNpc(src, context.getOne("npc"));
        NpcData data = npc.get(NpcData.class).orElseThrow(() -> new CommandException(NOT_AN_NPC));
        Messages.getNpcInfo(npc, data).sendTo(src);
        return CommandResult.success();
    }

    public CommandResult updateTraits(CommandSource src, CommandContext context) throws CommandException {
        Living npc = getTargetedNpc(src, context.getOne("npc"));
        NpcData npcData = npc.get(NpcData.class).orElseThrow(() -> new CommandException(NOT_AN_NPC));
        Set<NpcTrait> npcTraits = npcData.traits().get();
        int updates = 0;
        for (NpcTrait trait : this.plugin.game.getRegistry().getAllOf(NpcTrait.class)) {
            Optional<Boolean> value = context.getOne(trait.getId());
            if (value.isPresent()) {
                updates++;
                if (value.get())
                    npcTraits.add(trait);
                else
                    npcTraits.remove(trait);
            }
        }
        npc.offer(npcData.set(NpcKeys.TRAITS, npcTraits));
        src.sendMessage(UPDATED_TRAITS, ImmutableMap.of("amount", Text.of(updates)));
        return CommandResult.success();
    }

    @SuppressWarnings("unchecked")
    public CommandResult updateProperties(CommandSource src, CommandContext context) throws CommandException {
        Living npc = getTargetedNpc(src, context.getOne("npc"));
        NpcData npcData = npc.get(NpcData.class).orElseThrow(() -> new CommandException(NOT_AN_NPC));
        int updates = 0;
        for (NpcProperty prop : this.plugin.game.getRegistry().getAllOf(NpcProperty.class)) {
            String propId = prop.getId();
            if (context.hasAny(propId)) {
                Object value = context.getOne(propId).get();
                if (!prop.supports(value))
                    throw new CommandException(UNSUPPORTED_PROP_TYPE);
                try {
                    if (prop.set(npc, value, src))
                        updates++;
                } catch (PropertyException e) {
                    throw new CommandException(e.getText());
                }
            }
        }
        src.sendMessage(UPDATED_PROPS, ImmutableMap.of("amount", Text.of(updates)));
        return CommandResult.success();
    }

    private Living getTargetedNpc(CommandSource src, Optional<Entity> arg) throws CommandException {
        Entity npc;
        if (!arg.isPresent()) {
            if (!(src instanceof Player))
                throw new CommandException(MISSING_ENTITY);
            Player player = (Player) src;
            npc = player.getWorld().getIntersectingEntities(player, 10).stream()
                .filter(e -> !e.getEntity().equals(player)).findFirst()
                .map(EntityUniverse.EntityHit::getEntity).orElse(null);
        } else
            npc = arg.get();

        if (npc == null)
            throw new CommandException(NO_ENTITY_TARGET);
        if (!(npc instanceof Living))
            throw new CommandException(NOT_AN_NPC);

        return (Living) npc;
    }

}
