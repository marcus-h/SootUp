package tag;

import main.Tag;

public class IntOpTag implements Tag {
    public static final String NAME = "IntOpTag";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public byte[] getValue() {
        return new byte[1];
    }
}
