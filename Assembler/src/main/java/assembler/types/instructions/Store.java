package assembler.types.instructions;

import org.joou.UShort;
import assembler.types.ByteFormatter;
import assembler.types.NumberType;
import assembler.types.values.DeterminedByte;
import assembler.types.values.UndeterminedByte;

import java.util.HashMap;

public class Store extends Instruction {

    NumberType type;

    public Store(NumberType type){
        this.type = type;
    }

    private static final int[] STORE_ADDRESS_POINTED_BY_16 = new int[]{0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27};
    private static final int[] STORE_INDEXED = new int[]{0xD0, 0xD1, 0xD2, 0xD3, 0xD4, 0xD5, 0xD6, 0xD7};

    @Override
    public UShort compileInstruction(HashMap<UShort, UndeterminedByte> bytes, UShort currentPos, String... tokens) {
        validateInstruction(tokens);

        UndeterminedByte[] b;
        switch (type) {
            case ADDRESS_POINTED_BY_16: {
                int register = getRegisterIndex(tokens[1]);
                bytes.put(currentPos, DeterminedByte.valueOf(STORE_ADDRESS_POINTED_BY_16[register]));
                currentPos = currentPos.add(1);

                b = ByteFormatter.getWord(tokens[2]);
                bytes.put(currentPos, b[0]);
                currentPos = currentPos.add(1);

                bytes.put(currentPos, b[1]);
                currentPos = currentPos.add(1);
                break;
            }

            case INDEXED_ADDRESS: {
                String[] values = tokens[1].split(",");
                String label = values[0];
                String register = values[1];

                bytes.put(currentPos, DeterminedByte.valueOf(STORE_INDEXED[getRegisterIndex(register)]));
                currentPos = currentPos.add(1);

                b = ByteFormatter.getWord(label);
                bytes.put(currentPos, b[0]);
                currentPos = currentPos.add(1);

                bytes.put(currentPos, b[1]);
                currentPos = currentPos.add(1);
                break;
            }
        }

        return currentPos;
    }

    @Override
    public String getOpcode() {
        return "STORE";
    }

}
