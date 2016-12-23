package se.walkercrou.peeps.cmd.exe;

import static se.walkercrou.peeps.Messages.NOT_AN_NPC;
import static se.walkercrou.peeps.Messages.NO_LOCATION;
import static se.walkercrou.peeps.Messages.SPAWN_FAILED;
import static se.walkercrou.peeps.Messages.SPAWN_SUCCESS;
import static se.walkercrou.peeps.Messages.VERSION;
import static se.walkercrou.peeps.Messages.selfInsertingText;
import static se.walkercrou.peeps.cmd.CommandUtil.getTargetedEntity;
import static se.walkercrou.peeps.cmd.CommandUtil.getTargetedNpc;

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
import se.walkercrou.peeps.Messages;
import se.walkercrou.peeps.NpcSpawnException;
import se.walkercrou.peeps.Peeps;
import se.walkercrou.peeps.data.npc.NpcData;

import java.util.Optional;

public final class GeneralCommandExecutors {

    private final Peeps plugin;

    public GeneralCommandExecutors(Peeps plugin) {
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

    public CommandResult getTargetedEntityId(CommandSource src, CommandContext context) throws CommandException {
        Entity entity = getTargetedEntity(src, Optional.empty());
        src.sendMessage(selfInsertingText(Text.of(entity.getUniqueId())));
        return CommandResult.success();
    }

}
