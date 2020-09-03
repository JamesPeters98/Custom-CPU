package assembler.types.instructions.opcodes;

import assembler.types.instructions.interfaces.Instruction;
import org.joou.UShort;
import assembler.types.values.DeterminedByte;
import assembler.types.values.UndeterminedByte;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Any instruction that just takes a register as its operator. And follows the same hexadecimal row.
 */
public class RegisterRowInstruction extends Instruction {

    private String opcode;
    private int rowStart;

    public RegisterRowInstruction(String opcode, int rowStart){
        this.opcode = opcode;
        this.rowStart = rowStart;
    }

    @Override
    public UShort compileInstruction(HashMap<UShort, UndeterminedByte> bytes, UShort currentPos, String... tokens) {
        validateInstruction(tokens);

        int register = getRegisterIndex(tokens[1]);
        bytes.put(currentPos, DeterminedByte.valueOf(rowStart+register));

        return currentPos.add(1);
    }

    @Override
    protected List<Pattern[]> getPatternArray() {
        return createListPatternFromRegex("^r([0-7])");
    }

    @Override
    public String getOpcode() {
        return opcode;
    }
}
