package assembler.types.instructions;

import org.joou.UShort;
import assembler.types.ByteFormatter;
import assembler.types.NumberType;
import assembler.types.values.DeterminedByte;
import assembler.types.values.UndeterminedByte;

import java.util.HashMap;

public class LoadIndexed<T extends NumberType> extends Instruction {

    T t;

    public LoadIndexed(T t){
        this.t = t;
    }

    private static final int[] opcodes = new int[]{0xC0, 0xC1, 0xC2, 0xC3, 0xC4, 0xC5, 0xC6, 0xC7};

    @Override
    public UShort compileInstruction(HashMap<UShort, UndeterminedByte> bytes, UShort currentPos, String... tokens) {
        validateInstruction(tokens);

        String registerStr = tokens[1];
        int register = -1;
        try {
            register = Integer.parseInt(registerStr.replaceAll("r", ""));
        } catch (NumberFormatException e){
            e.printStackTrace();
            System.exit(-1);
        }

        // LOAD Immediate byte
        UndeterminedByte[] b;
        if(t == NumberType.INDEXED_ADDRESS){
            bytes.put(currentPos, DeterminedByte.valueOf(opcodes[register]));
            currentPos = currentPos.add(1);

            b = ByteFormatter.getWord(tokens[2]);
            bytes.put(currentPos, b[0]);
            currentPos = currentPos.add(1);

            bytes.put(currentPos, b[1]);
            currentPos = currentPos.add(1);
        }

        return currentPos;
    }

    @Override
    public String getOpcode() {
        return "LOADI";
    }

}
