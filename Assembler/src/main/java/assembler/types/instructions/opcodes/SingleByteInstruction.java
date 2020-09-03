package assembler.types.instructions.opcodes;

import assembler.types.instructions.interfaces.Instruction;
import org.joou.UShort;
import assembler.types.values.DeterminedByte;
import assembler.types.values.UndeterminedByte;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class SingleByteInstruction extends Instruction {

    private final String identifier;
    private final int opcode;

    public SingleByteInstruction(String identifier, int opcode){
        this.identifier = identifier;
        this.opcode = opcode;
    }

    @Override
    public UShort compileInstruction(HashMap<UShort, UndeterminedByte> bytes, UShort currentPos, String... tokens) {
        validateInstruction(tokens);
        bytes.put(currentPos, DeterminedByte.valueOf(opcode));
        return currentPos.add(1);
    }

    @Override
    protected List<Pattern[]> getPatternArray() {
        // No patterns for single byte instructions.
        return createListPatternFromRegex();
    }

    @Override
    public String getOpcode() {
        return identifier;
    }
}
