package assembler.types.instructions;

import org.joou.UShort;
import assembler.types.values.DeterminedByte;
import assembler.types.values.UndeterminedByte;

import java.util.HashMap;

public class Inc extends Instruction {

    private static final int[] opcodes = new int[]{0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F};

    @Override
    public UShort compileInstruction(HashMap<UShort, UndeterminedByte> bytes, UShort currentPos, String... tokens) {
        validateInstruction(tokens);

        int register = getRegisterIndex(tokens[1]);
        bytes.put(currentPos, DeterminedByte.valueOf(opcodes[register]));

        return currentPos.add(1);
    }

    @Override
    public String getOpcode() {
        return "INC";
    }
}
