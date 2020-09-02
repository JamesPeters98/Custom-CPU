package assembler.types.instructions;

import org.joou.UShort;
import assembler.types.values.DeterminedByte;
import assembler.types.values.UndeterminedByte;

import java.util.HashMap;

public class Halt extends Instruction {

    @Override
    public UShort compileInstruction(HashMap<UShort, UndeterminedByte> bytes, UShort currentPos, String... tokens) {
        validateInstruction(tokens);
        bytes.put(currentPos, DeterminedByte.valueOf(0x01));
        return currentPos.add(1);
    }

    @Override
    public String getOpcode() {
        return "HALT";
    }
}
