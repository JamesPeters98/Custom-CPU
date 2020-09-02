package assembler.types.instructions;

import org.joou.UShort;
import assembler.types.ByteFormatter;
import assembler.types.NumberType;
import assembler.types.values.DeterminedByte;
import assembler.types.values.UndeterminedByte;

import java.util.HashMap;

public class Load<T extends NumberType> extends Instruction {

    T t;

    public Load(T t){
        this.t = t;
    }

    private static final int[] LOAD_OPCODES = new int[]{0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17};
    private static final int[] LOAD_INDEXED_OPCODES = new int[]{0xC0, 0xC1, 0xC2, 0xC3, 0xC4, 0xC5, 0xC6, 0xC7};

    @Override
    public UShort compileInstruction(HashMap<UShort, UndeterminedByte> bytes, UShort currentPos, String... tokens) {
        validateInstruction(tokens);

        // LOAD Immediate byte
        if(t == NumberType.IMMEDIATE_BYTE){
            int register = getRegisterIndex(tokens[1]);
            bytes.put(currentPos, DeterminedByte.valueOf(LOAD_OPCODES[register]));
            currentPos = currentPos.add(1);

            DeterminedByte b = ByteFormatter.getByte(tokens[2]);
            bytes.put(currentPos, b);
            currentPos = currentPos.add(1);
        }


        if(t == NumberType.INDEXED_ADDRESS){
            String[] values = tokens[1].split(",");
            String label = values[0];
            String register = values[1];

            bytes.put(currentPos, DeterminedByte.valueOf(LOAD_INDEXED_OPCODES[getRegisterIndex(register)]));
            currentPos = currentPos.add(1);

            UndeterminedByte[] b = ByteFormatter.getWord(label);
            bytes.put(currentPos, b[0]);
            currentPos = currentPos.add(1);

            bytes.put(currentPos, b[1]);
            currentPos = currentPos.add(1);
        }

        return currentPos;
    }

    @Override
    public String getOpcode() {
        return "LOAD";
    }

}
