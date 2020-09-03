package assembler.types.instructions.opcodes;

import assembler.types.ByteFormatter;
import assembler.types.NumberType;
import assembler.types.instructions.interfaces.Conditional;
import assembler.types.values.DeterminedByte;
import assembler.types.values.UndeterminedByte;
import org.joou.UShort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class ConditionalAddressPointer extends Conditional {

    NumberType numberType;
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
     * @param numberType
     * @param conditionOpcodes:
     *                        0 -> UNCONDITIONAL
     *                        1 -> NOT_ZERO
     *                        2 -> IF_ZERO
     */
    public ConditionalAddressPointer(String opcode, NumberType numberType, int... conditionOpcodes){
        this.numberType = numberType;
        this.opcodes = conditionOpcodes;
        this.opcode = opcode;
    }

    @Override
    public UShort compileInstruction(HashMap<UShort, UndeterminedByte> bytes, UShort currentPos, String... tokens) {
        validateInstruction(tokens);

        String conditional = tokens[1];
        Conditional.Type type = getConditional(conditional);

        bytes.put(currentPos, DeterminedByte.valueOf(getOpCode(type)));
        currentPos = currentPos.add(1);

        // If type if unconditional that means the token is back 1.
        String operand = tokens[1];
        if(type != Type.UNCONDITIONAL) operand = tokens[2];

        UndeterminedByte[] b;
        if(numberType == NumberType.ADDRESS_POINTED_BY_16){
            b = ByteFormatter.getWord(operand);
            bytes.put(currentPos, b[0]);
            currentPos = currentPos.add(1);

            bytes.put(currentPos, b[1]);
            currentPos = currentPos.add(1);
        }

        return currentPos;
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
