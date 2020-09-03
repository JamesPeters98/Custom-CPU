package assembler.types.instructions.opcodes;

import assembler.types.instructions.interfaces.Instruction;
import assembler.types.values.DeterminedByte;
import assembler.types.values.UndeterminedByte;
import org.joou.UShort;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Any instruction that just takes a 16 bit register as its operator.
 */
public class Register16BitInstruction extends Instruction {

    private String opcode;
    private int opcodeVal;

    public Register16BitInstruction(String opcode, int opcodeVal){
        this.opcode = opcode;
        this.opcodeVal = opcodeVal;
    }

    @Override
    public UShort compileInstruction(HashMap<UShort, UndeterminedByte> bytes, UShort currentPos, String... tokens) {
        validateInstruction(tokens);
        bytes.put(currentPos, DeterminedByte.valueOf(opcodeVal));
        return currentPos.add(1);
    }

    @Override
    protected List<Pattern[]> getPatternArray() {
        return createListPatternFromRegex("^r16$");
    }

    @Override
    public String getOpcode() {
        return opcode;
    }
}
