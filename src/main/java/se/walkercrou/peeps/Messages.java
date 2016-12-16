package se.walkercrou.peeps;

import static org.spongepowered.api.text.Text.NEW_LINE;
import static org.spongepowered.api.text.TextTemplate.arg;
import static org.spongepowered.api.text.TextTemplate.of;
import static org.spongepowered.api.text.format.TextColors.BLUE;
import static org.spongepowered.api.text.format.TextColors.GRAY;
import static org.spongepowered.api.text.format.TextColors.GREEN;
import static org.spongepowered.api.text.format.TextColors.RED;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextTemplate;

public final class Messages {

    public static final TextTemplate VERSION = of(GREEN, arg("plugin.name"), " v", arg("plugin.version").color(BLUE));

    public static final Text NO_LOCATION = Text.of(RED, "You must enter a location if you are not a player.");

    public static final Text SPAWN_FAILED = Text.of(RED, "Could not spawn NPC here.");

    public static final TextTemplate SPAWN_SUCCESS = of(
        Text.of(GREEN, "Spawned a new NPC!"),                               NEW_LINE, BLUE,
        "Owner            ",    arg("npc.owner").color(GRAY),           NEW_LINE,
        "Entity Type      ",    arg("npc.entity.type").color(GRAY),     NEW_LINE,
        "Unique ID        ",    arg("npc.entity.uuid").color(GRAY),     NEW_LINE,
        "Display Name    ",     arg("npc.displayName").color(GRAY));

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

    private Messages() {}

}
