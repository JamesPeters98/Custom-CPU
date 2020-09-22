package assembler.types.instructions.opcodes;

import assembler.types.ByteFormatter;
import assembler.types.instructions.interfaces.Instruction;
import assembler.types.values.DeterminedByte;
import assembler.types.values.UndeterminedByte;
import org.joou.UShort;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class AddressPointer extends Instruction {

    private String opcode;
    private int opcodeValue;

    public AddressPointer(String opcode, int opcodeValue){
        this.opcode = opcode;
        this.opcodeValue = opcodeValue;
    }

    @Override
    public UShort compileInstruction(HashMap<UShort, UndeterminedByte> bytes, UShort currentPos, String... tokens) {
        validateInstruction(tokens);

        bytes.put(currentPos, DeterminedByte.valueOf(opcodeValue));
        currentPos = currentPos.add(1);

        String word = tokens[1];
        UndeterminedByte[] b = ByteFormatter.getWord(word);

        bytes.put(currentPos, b[0]);
        currentPos = currentPos.add(1);

        bytes.put(currentPos, b[1]);
        currentPos = currentPos.add(1);

        return currentPos;
    }

    @Override
    protected List<Pattern[]> getPatternArray() {
        return createListPatternFromRegex("^.*$");
    }

    @Override
    public String getOpcode() {
        return opcode;
    }
}
