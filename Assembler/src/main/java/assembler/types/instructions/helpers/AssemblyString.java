package assembler.types.instructions.helpers;

import org.joou.UShort;
import assembler.types.instructions.interfaces.Instruction;
import assembler.types.values.DeterminedByte;
import assembler.types.values.UndeterminedByte;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Stores an ASCII String in binary at the current location
 */
public class AssemblyString extends Instruction {

    private static final String ASCII = "\"";

    @Override
    public UShort compileInstruction(HashMap<UShort, UndeterminedByte> bytes, UShort currentPos, String... tokens) {
        validateInstruction(tokens);
        String string = tokens[1];
        if(string.startsWith(ASCII) && string.endsWith(ASCII)) {
            string = string.substring(1, string.length()-1)+"\0";
            for (char c : string.toCharArray()) {
                bytes.put(currentPos, DeterminedByte.valueOf(c));
                currentPos = currentPos.add(1);
            }
        } else {
            throw new RuntimeException("String must start and end with quotes \" \" invalid for String: "+ Arrays.toString(tokens));
        }
        return currentPos;
    }

    @Override
    protected List<Pattern[]> getPatternArray() {
        return createListPatternFromRegex("^\\\"(.*)\\\"$");
    }

    @Override
    public String getOpcode() {
        return "String";
    }


}
