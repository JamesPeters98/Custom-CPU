package assembler.types.instructions;

import org.joou.UShort;
import assembler.types.values.DeterminedByte;
import assembler.types.values.UndeterminedByte;

import java.util.HashMap;

public class Dec extends Instruction {

    private static final int[] opcodes = new int[]{0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F};

    @Override
    public UShort compileInstruction(HashMap<UShort, UndeterminedByte> bytes, UShort currentPos, String... tokens) {
        validateInstruction(tokens);

        int register = getRegisterIndex(tokens[1]);
        bytes.put(currentPos, DeterminedByte.valueOf(opcodes[register]));

        return currentPos.add(1);
    }

    @Override
    public String getOpcode() {
        return "DEC";
    }
}
