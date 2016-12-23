package se.walkercrou.peeps.cmd.exe;

import static se.walkercrou.peeps.Messages.CLEARED_PROPS;
import static se.walkercrou.peeps.Messages.NOT_AN_NPC;
import static se.walkercrou.peeps.Messages.UNSUPPORTED_PROP_TYPE;
import static se.walkercrou.peeps.Messages.UPDATED_PROPS;
import static se.walkercrou.peeps.cmd.CommandUtil.*;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.text.Text;
import se.walkercrou.peeps.Peeps;
import se.walkercrou.peeps.data.npc.NpcData;
import se.walkercrou.peeps.property.NpcProperty;
import se.walkercrou.peeps.property.PropertyException;

public final class PropertyCommandExecutors {

    private final Peeps plugin;

    public PropertyCommandExecutors(Peeps plugin) {
        this.plugin = plugin;
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

    public CommandResult clearProperties(CommandSource src, CommandContext context) throws CommandException {
        Living npc = getTargetedNpc(src, context.getOne("npc"));
        NpcData npcData = npc.get(NpcData.class).orElseThrow(() -> new CommandException(NOT_AN_NPC));
        int cleared = 0;
        for (NpcProperty prop : this.plugin.game.getRegistry().getAllOf(NpcProperty.class)) {
            String propId = prop.getId();
            if (context.hasAny(propId)) {
                try {
                    if (prop.clear(npc, src))
                        cleared++;
                } catch (PropertyException e) {
                    throw new CommandException(e.getText());
                }
            }
        }
        src.sendMessage(CLEARED_PROPS, ImmutableMap.of("amount", Text.of(cleared)));
        return CommandResult.success();
    }

}
