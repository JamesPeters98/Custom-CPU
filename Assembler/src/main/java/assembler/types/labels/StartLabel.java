package assembler.types.labels;

import org.joou.UShort;
import assembler.types.values.UndeterminedValue;

public class StartLabel implements Label {

    private static final UShort ENTRY_POINT = UShort.valueOf(0xA0);

    @Override
    public UShort onLabelFound(UShort currentPos, String... tokens) {
        String labelName = tokens[0].replace(":", "");
        UndeterminedValue.setUndeterminedWord(labelName, ENTRY_POINT);
        return ENTRY_POINT;
    }

    @Override
    public UShort getLabelAddress() {
        return ENTRY_POINT;
    }
}
