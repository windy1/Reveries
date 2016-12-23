package se.walkercrou.peeps.cmd.exe;

import static se.walkercrou.peeps.Messages.NOT_AN_NPC;
import static se.walkercrou.peeps.Messages.UPDATED_TRAITS;
import static se.walkercrou.peeps.cmd.CommandUtil.getTargetedNpc;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.text.Text;
import se.walkercrou.peeps.Peeps;
import se.walkercrou.peeps.data.NpcKeys;
import se.walkercrou.peeps.data.npc.NpcData;
import se.walkercrou.peeps.trait.NpcTrait;

import java.util.Optional;
import java.util.Set;

public final class TraitCommandExecutors {

    private final Peeps plugin;

    public TraitCommandExecutors(Peeps plugin) {
        this.plugin = plugin;
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

}
