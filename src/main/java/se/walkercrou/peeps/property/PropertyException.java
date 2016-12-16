package se.walkercrou.peeps.property;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.TextMessageException;

import javax.annotation.Nonnull;

public final class PropertyException extends TextMessageException {

    public PropertyException(Text msg) {
        super(msg);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    @Nonnull
    public Text getText() {
        return super.getText();
    }

}
