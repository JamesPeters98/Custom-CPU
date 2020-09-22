package assembler.types.instructions.helpers;

import assembler.types.ByteFormatter;
import assembler.types.instructions.interfaces.Instruction;
import assembler.types.values.UndeterminedByte;
import org.joou.UShort;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class AssemblyWord extends Instruction {

    @Override
    public UShort compileInstruction(HashMap<UShort, UndeterminedByte> bytes, UShort currentPos, String... tokens) {
        validateInstruction(tokens);
        String word = tokens[1];

        UndeterminedByte[] val = ByteFormatter.getWord(word);
        bytes.put(currentPos, val[0]);
        currentPos = currentPos.add(1);

        bytes.put(currentPos, val[1]);
        currentPos = currentPos.add(1);

        return currentPos;
    }

    @Override
    protected List<Pattern[]> getPatternArray() {
        return createListPatternFromRegex("^(.*)$");
    }

    @Override
    public String getOpcode() {
        return ".word";
    }
}
