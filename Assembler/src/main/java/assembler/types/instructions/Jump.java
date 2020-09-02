package assembler.types.instructions;

import org.joou.UShort;
import assembler.types.ByteFormatter;
import assembler.types.NumberType;
import assembler.types.values.DeterminedByte;
import assembler.types.values.UndeterminedByte;

import java.util.HashMap;

public class Jump<T extends NumberType> extends Instruction {

    T t;
    Type type;

    enum Type {
        UNCONDITIONAL(0x02),
        IF_NOT_ZERO(0x03),
        IF_ZERO(0x04);

        final int opcode;

        Type(int opcode) {
            this.opcode = opcode;
        }
    }

    public Jump(T t, Type type){
        this.t = t;
        this.type = type;
    }

//    private static final int[] opcodes = new int[]{0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27};

    @Override
    public UShort compileInstruction(HashMap<UShort, UndeterminedByte> bytes, UShort currentPos, String... tokens) {
        validateInstruction(tokens);

        bytes.put(currentPos, DeterminedByte.valueOf(type.opcode));
        currentPos = currentPos.add(1);

        UndeterminedByte[] b;
        if(t == NumberType.ADDRESS_POINTED_BY_16){
            b = ByteFormatter.getWord(tokens[1]);
            bytes.put(currentPos, b[0]);
            currentPos = currentPos.add(1);

            bytes.put(currentPos, b[1]);
            currentPos = currentPos.add(1);
        }

        return currentPos;
    }

    @Override
    public String getOpcode() {
        switch (type){
            default:
                return "JUMP";
            case IF_ZERO:
                return "JZ";
            case IF_NOT_ZERO:
                return "JNZ";
        }
    }

}
