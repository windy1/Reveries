package se.walkercrou.peeps.cmd;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.text.Text;
import se.walkercrou.peeps.Messages;

import java.util.List;
import java.util.UUID;

public final class SkinIdCommandElement extends CommandElement {

    protected SkinIdCommandElement(Text key) {
        super(key);
    }

    @Override
    protected Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        String str = args.next();
        try {
            return UUID.fromString(str);
        } catch (IllegalArgumentException e) {
            throw args.createError(Messages.INVALID_UUID);
        }
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        return null;
    }

}
