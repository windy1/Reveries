package se.walkercrou.peeps;

import static org.spongepowered.api.text.Text.NEW_LINE;
import static org.spongepowered.api.text.TextTemplate.arg;
import static org.spongepowered.api.text.TextTemplate.of;
import static org.spongepowered.api.text.action.TextActions.runCommand;
import static org.spongepowered.api.text.action.TextActions.suggestCommand;
import static org.spongepowered.api.text.format.TextColors.BLUE;
import static org.spongepowered.api.text.format.TextColors.GRAY;
import static org.spongepowered.api.text.format.TextColors.GREEN;
import static org.spongepowered.api.text.format.TextColors.RED;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.GameRegistry;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextElement;
import org.spongepowered.api.text.TextTemplate;
import se.walkercrou.peeps.data.mutable.NpcData;
import se.walkercrou.peeps.property.NpcProperty;
import se.walkercrou.peeps.trait.NpcTrait;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public final class Messages {

    public static final TextTemplate VERSION = of(GREEN, arg("plugin.name"), " v", arg("plugin.version").color(BLUE));

    public static final Text NO_LOCATION = Text.of(RED, "You must enter a location if you are not a player.");

    public static final Text SPAWN_FAILED = Text.of(RED, "Could not spawn NPC here.");

    public static final Text SPAWN_SUCCESS = Text.of(GREEN, "Spawned a new NPC!");

    public static final TextTemplate NPC_INFO = of(
        BLUE,
        "Owner            ",    arg("npc.owner").color(GRAY),           NEW_LINE,
        "Entity Type      ",    arg("npc.entity.type").color(GRAY),     NEW_LINE,
        "Unique ID        ",    arg("npc.entity.uuid").color(GRAY),     NEW_LINE,
        "Traits", NEW_LINE,     arg("npc.traits"),                      NEW_LINE,
        "Properties", NEW_LINE, arg("npc.properties"));

    public static final Text DEFAULT_DISPLAY_NAME = Text.of("Unnamed NPC");

    public static final Text NONE = Text.of("[NONE]");

    public static final Text NOT_AN_NPC = Text.of(RED, "The specified entity is not an NPC.");

    public static final TextTemplate UPDATED_TRAITS = of(GREEN, "Updated ", arg("amount"), " traits.");

    public static final Text IMMUTABLE_SKIN = Text.of(RED, "This entity cannot change their skin.");

    public static final Text INVALID_UUID = Text.of(RED, "Invalid UUID.");

    public static final TextTemplate UPDATED_PROPS = of(GREEN, "Updated ", arg("amount"), " properties.");

    public static final Text SKIN_NOT_FOUND = Text.of(RED, "Skin not found.");

    public static final Text SKIN_LOOKUP = Text.of(GREEN, "Looking up skin...");

    public static final Text NO_AI = Text.of(RED, "This NPC cannot have AI.");

    public static final Text UNSUPPORTED_PROP_TYPE = Text.of(RED, "Invalid property value type.");

    public static final Text MISSING_ENTITY = Text.of(
        RED, "You must enter an NPC ID when performing this from the command line.");

    public static final Text NO_ENTITY_TARGET = Text.of(RED, "No entity target found.");

    private Messages() {}

    public static PaginationList getNpcInfo(Living npc, NpcData npcData) {
        Map<String, TextElement> info = Maps.newHashMap();
        info.put("npc.owner", Text.of(npcData.ownerId().get()));
        info.put("npc.entity.type", Text.of(npc.getType().getId()));
        info.put("npc.entity.uuid", Text.of(npc.getUniqueId()));

        GameRegistry reg = Sponge.getRegistry();
        UUID npcId = npc.getUniqueId();

        Text.Builder traitsBuilder = Text.builder();
        Set<NpcTrait> npcTraits = npcData.traits().get();
        List<NpcTrait> allTraits = Lists.newArrayList(reg.getAllOf(NpcTrait.class));
        allTraits.sort(Comparator.comparing(CatalogType::getId));
        for (int i = 0; i < allTraits.size(); i++) {
            NpcTrait trait = allTraits.get(i);
            boolean enabled = npcTraits.contains(trait);
            traitsBuilder.append(Text.builder()
                .append(Text.of("  ", enabled ? GREEN : RED, trait.getId()))
                .onClick(runCommand("/npc trait " + npcId + " --" + trait.getId() + "=" + !enabled))
                .build());
            if (i < allTraits.size() - 1)
                traitsBuilder.append(NEW_LINE);
        }
        info.put("npc.traits", traitsBuilder);

        Text.Builder propsBuilder = Text.builder();
        List<NpcProperty> props = Lists.newArrayList(reg.getAllOf(NpcProperty.class));
        props.sort(Comparator.comparing(CatalogType::getId));
        for (int i = 0; i < props.size(); i++) {
            NpcProperty prop = props.get(i);
            Optional<?> value = prop.get(npc);
            propsBuilder.append(Text.builder()
                .append(Text.of(
                    "  ", GREEN, prop.getId(), GRAY, "=",
                    value.map(v -> Text.of(GREEN, v)).orElse(Text.of(RED, "None"))))
                .onClick(suggestCommand("/npc prop " + npcId + " --" + prop.getId() + "="))
                .build());
            if (i < props.size() - 1)
                propsBuilder.append(NEW_LINE);
        }
        info.put("npc.properties", propsBuilder);

        return PaginationList.builder()
            .title(Text.builder("NPC").color(GRAY).build())
            .contents(NPC_INFO.apply(info).build())
            .build();
    }

}
