package se.walkercrou.peeps.cmd;

import static org.spongepowered.api.command.args.GenericArguments.bool;
import static org.spongepowered.api.command.args.GenericArguments.catalogedElement;
import static org.spongepowered.api.command.args.GenericArguments.doubleNum;
import static org.spongepowered.api.command.args.GenericArguments.entity;
import static org.spongepowered.api.command.args.GenericArguments.flags;
import static org.spongepowered.api.command.args.GenericArguments.location;
import static org.spongepowered.api.command.args.GenericArguments.onlyOne;
import static org.spongepowered.api.command.args.GenericArguments.optional;
import static org.spongepowered.api.command.args.GenericArguments.remainingJoinedStrings;
import static org.spongepowered.api.command.args.GenericArguments.string;
import static org.spongepowered.api.command.args.GenericArguments.vector3d;

import org.spongepowered.api.command.CommandMapping;
import org.spongepowered.api.command.args.CommandFlags;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.text.Text;
import se.walkercrou.peeps.Peeps;
import se.walkercrou.peeps.cmd.exe.GeneralCommandExecutors;
import se.walkercrou.peeps.cmd.exe.PropertyCommandExecutors;
import se.walkercrou.peeps.cmd.exe.TraitCommandExecutors;
import se.walkercrou.peeps.trait.NpcTrait;

public final class CommandRegistrar {

    private final Peeps plugin;
    private CommandMapping rootMapping;

    public CommandRegistrar(Peeps plugin) {
        this.plugin = plugin;
    }

    public void register() {
        GeneralCommandExecutors general = new GeneralCommandExecutors(this.plugin);
        TraitCommandExecutors traits = new TraitCommandExecutors(this.plugin);

        CommandSpec create = CommandSpec.builder()
            .executor(general::createNpc)
            .arguments(
                optional(onlyOne(catalogedElement(Text.of("entityType"), EntityType.class))),
                optional(onlyOne(location(Text.of("location")))),
                optional(remainingJoinedStrings(Text.of("displayName"))))
            .build();

        CommandSpec getInfo = CommandSpec.builder()
            .executor(general::getNpcInfo)
            .arguments(optional(onlyOne(entity(Text.of("npc")))))
            .build();

        CommandFlags.Builder traitFlags = flags();
        for (NpcTrait trait : this.plugin.game.getRegistry().getAllOf(NpcTrait.class))
            traitFlags.valueFlag(bool(Text.of(trait.getId())), "-" + trait.getId());

        CommandSpec trait = CommandSpec.builder()
            .executor(traits::updateTraits)
            .arguments(traitFlags.buildWith(optional(onlyOne(entity(Text.of("npc"))))))
            .build();

        PropertyCommandExecutors props = new PropertyCommandExecutors(this.plugin);
        CommandSpec setProps = CommandSpec.builder()
            .executor(props::updateProperties)
            .arguments(flags()
                .valueFlag(string(Text.of("skin")), "-skin")
                .valueFlag(remainingJoinedStrings(Text.of("displayName")), "-displayName")
                .valueFlag(vector3d(Text.of("rotation")), "-rotation")
                .valueFlag(location(Text.of("location")), "-location")
                .valueFlag(entity(Text.of("riding")), "-riding")
                .valueFlag(doubleNum(Text.of("sight")), "-sight")
                .valueFlag(bool(Text.of("nameTag")), "-nameTag")
                .buildWith(optional(onlyOne(entity(Text.of("npc"))))))
            .build();

        CommandSpec clearProps = CommandSpec.builder()
            .executor(props::clearProperties)
            .arguments(flags()
                .flag("-skin")
                .flag("-displayName")
                .flag("-rotation")
                .flag("-location")
                .flag("-riding")
                .flag("-sight")
                .flag("-nameTag")
                .buildWith(optional(onlyOne(entity(Text.of("npc"))))))
            .build();

        CommandSpec entityId = CommandSpec.builder().executor(general::getTargetedEntityId).build();

        CommandSpec root = CommandSpec.builder()
            .executor(general::showVersion)
            .child(create, "create", "new", "mk", "spawn")
            .child(getInfo, "getinfo", "info", "information", "data")
            .child(trait, "trait")
            .child(setProps, "prop", "propset")
            .child(clearProps, "propclear")
            .child(entityId, "entityid", "eid", "getid")
            .build();

        this.rootMapping = this.plugin.game.getCommandManager()
            .register(this.plugin, root, "npc", "peeps")
            .orElse(null);
    }

    public void deregister() {
        if (this.rootMapping != null)
            this.plugin.game.getCommandManager().removeMapping(this.rootMapping);
    }

}
