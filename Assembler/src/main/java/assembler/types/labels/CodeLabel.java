package assembler.types.labels;

import org.joou.UShort;
import assembler.types.values.UndeterminedValue;

public class CodeLabel implements Label {
    @Override
    public UShort onLabelFound(UShort currentPos, String... tokens) {
        // Sets the address for this label to be used elsewhere in the compiler.
        String labelName = tokens[0].replace(":", "");
        UndeterminedValue.setUndeterminedWord(labelName, currentPos);
        return currentPos;
    }

    @Override
    public UShort getLabelAddress() {
        return null;
    }
}
