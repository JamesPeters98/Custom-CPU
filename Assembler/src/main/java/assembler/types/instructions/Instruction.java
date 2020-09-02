package assembler.types.instructions;

import org.joou.UShort;
import assembler.types.values.UndeterminedByte;

import java.util.HashMap;

public abstract class Instruction {

    protected void validateInstruction(String[] tokens){
        String str = tokens[0];
        if(!str.equals(getOpcode())){
            throw new IllegalArgumentException(getOpcode()+" instruction called but token = "+str);
        }
    }

    public abstract UShort compileInstruction(HashMap<UShort, UndeterminedByte> bytes, UShort currentPos, String... tokens);

    public int getRegisterIndex(String registerStr) {
        try {
            return Integer.parseInt(registerStr.replaceAll("r", ""));
        } catch (NumberFormatException e){
            e.printStackTrace();
            System.exit(-1);
        }
        return -1;
    }

    // Returns the token for this opcode e.g 'NOP'
    public abstract String getOpcode();
}
