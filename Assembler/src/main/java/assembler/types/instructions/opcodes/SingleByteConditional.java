package assembler.types.instructions.opcodes;

import assembler.types.NumberType;
import assembler.types.instructions.interfaces.Conditional;
import assembler.types.values.DeterminedByte;
import assembler.types.values.UndeterminedByte;
import org.joou.UShort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class SingleByteConditional extends Conditional {

    int[] opcodes;
    String opcode;

    @Override
    public int getOpCode(Type type) {
        switch (type){
            case UNCONDITIONAL: return opcodes[0];
            case NOT_ZERO: return opcodes[1];
            case IF_ZERO: return opcodes[2];
        }
        throw new RuntimeException("Invalid Conditional Type");
    }

    /**
     *
     * @param conditionOpcodes:
     *                        0 -> UNCONDITIONAL
     *                        1 -> NOT_ZERO
     *                        2 -> IF_ZERO
     */
    public SingleByteConditional(String opcode, int... conditionOpcodes){
        this.opcodes = conditionOpcodes;
        this.opcode = opcode;
    }

    @Override
    public UShort compileInstruction(HashMap<UShort, UndeterminedByte> bytes, UShort currentPos, String... tokens) {
        validateInstruction(tokens);
        Conditional.Type conditional = getConditional((tokens[1] != null) ? tokens[1] : "");
        bytes.put(currentPos, DeterminedByte.valueOf(getOpCode(conditional)));
        return currentPos.add(1);
    }

    @Override
    protected List<Pattern[]> getPatternArray() {
        // Currently just accepts anything and passes it to ByteFormatter which will catch errors.
        List<Pattern[]> patterns = new ArrayList<>();
        patterns.add(createPatternFromRegex("^.*$")); // Matches unconditional line
        patterns.add(createPatternFromRegex("^([A-Z]*)$", "^.*$")); // Matches conditional lines

        return patterns;
    }

    @Override
    public String getOpcode() {
        return opcode;
    }
}
