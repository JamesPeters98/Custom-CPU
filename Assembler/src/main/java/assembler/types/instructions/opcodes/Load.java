package assembler.types.instructions.opcodes;

import assembler.types.instructions.interfaces.Instruction;
import org.joou.UShort;
import assembler.types.ByteFormatter;
import assembler.types.NumberType;
import assembler.types.values.DeterminedByte;
import assembler.types.values.UndeterminedByte;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class Load extends Instruction {

    NumberType numberType;

    public Load(NumberType numberType){
        this.numberType = numberType;
    }

    private static final int[] LOAD_OPCODES = new int[]{0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17};
    private static final int[] LOAD_INDEXED_OPCODES = new int[]{0xC0, 0xC1, 0xC2, 0xC3, 0xC4, 0xC5, 0xC6, 0xC7};
    private static final int LOAD_REGISTER_OPCODE_INDEX = 0x30;
    private static final int LOAD_REGISTER_16bit_OPCODE_INDEX = 0xF0;
    private static final int[] LOAD_ADDRESS_FROM_REGISTER_16bit_OPCODE_INDEX = new int[]{0xE0,0xE1,0xE2,0xE3,0xE4,0xE5,0xE6,0xE7};

    @Override
    public UShort compileInstruction(HashMap<UShort, UndeterminedByte> bytes, UShort currentPos, String... tokens) {
        validateInstruction(tokens);

        // LOAD Immediate byte
        if(numberType == NumberType.IMMEDIATE_BYTE){
            int register = getRegisterIndex(tokens[1]);
            bytes.put(currentPos, DeterminedByte.valueOf(LOAD_OPCODES[register]));
            currentPos = currentPos.add(1);

            DeterminedByte b = ByteFormatter.getByte(tokens[2]);
            bytes.put(currentPos, b);
            currentPos = currentPos.add(1);
        }

        // LOAD indexed address using next byte and register as index.
        if(numberType == NumberType.INDEXED_ADDRESS){
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

        // LOAD register into register
        if(numberType == NumberType.REGISTER_VALUE){
            String[] values = tokens[1].split(",");
            String registerDestStr = values[0];
            String registerSourceStr = values[1];

            int registerDest = getRegisterIndex(registerDestStr);
            int registerSource = getRegisterIndex(registerSourceStr);
            int opcode = LOAD_REGISTER_OPCODE_INDEX + (registerDest + 8 * registerSource);

            bytes.put(currentPos, DeterminedByte.valueOf(opcode));
            currentPos = currentPos.add(1);
        }

        // LOAD address into 16bit register
        if(numberType == NumberType.REGISTER_16_BIT){
            String[] values = tokens[1].split(",");
            String a16 = values[1];

            bytes.put(currentPos, DeterminedByte.valueOf(LOAD_REGISTER_16bit_OPCODE_INDEX));
            currentPos = currentPos.add(1);

            UndeterminedByte[] b = ByteFormatter.getWord(a16);
            bytes.put(currentPos, b[0]);
            currentPos = currentPos.add(1);

            bytes.put(currentPos, b[1]);
            currentPos = currentPos.add(1);
        }

        // Loads value pointed to by 16bit register into register r
        if (numberType == NumberType.ADDRESS_FROM_16BIT_REGISTER) {
            String[] values = tokens[1].split(",");
            String register = values[0];

            int r = getRegisterIndex(register);
            bytes.put(currentPos, DeterminedByte.valueOf(LOAD_ADDRESS_FROM_REGISTER_16bit_OPCODE_INDEX[r]));
            currentPos = currentPos.add(1);
        }

        return currentPos;
    }

    @Override
    protected List<Pattern[]> getPatternArray() {
        switch (numberType){
            case IMMEDIATE_BYTE: return createListPatternFromRegex("^r([0-7])", "^\\#.*$");
            case INDEXED_ADDRESS: return createListPatternFromRegex("^(.*),r([0-7])$");
            case REGISTER_VALUE: return  createListPatternFromRegex("^r([0-7]+),r([0-7])$");
            case REGISTER_16_BIT: return createListPatternFromRegex("^r16,(.*)$");
            case ADDRESS_FROM_16BIT_REGISTER: return createListPatternFromRegex("^r([0-7]),r16$");
        }

        return createListPatternFromRegex();
    }

    @Override
    public String getOpcode() {
        return "LOAD";
    }

}
