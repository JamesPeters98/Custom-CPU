package assembler.types.instructions.enums;

import assembler.types.NumberType;
import assembler.types.instructions.helpers.AssemblyWord;
import assembler.types.instructions.opcodes.AddressPointer;
import assembler.types.instructions.opcodes.ConditionalAddressPointer;
import assembler.types.instructions.opcodes.Load;
import assembler.types.instructions.opcodes.Register16BitInstruction;
import assembler.types.instructions.opcodes.RegisterRowInstruction;
import assembler.types.instructions.opcodes.SingleByteConditional;
import assembler.types.instructions.opcodes.SingleByteInstruction;
import assembler.types.instructions.opcodes.Store;
import assembler.types.instructions.helpers.AssemblyString;
import assembler.types.instructions.interfaces.Instruction;

import java.util.regex.Pattern;

public enum OpCode {

    NOP(new SingleByteInstruction("NOP", 0x00)),
    HALT(new SingleByteInstruction("HALT", 0x01)),
    LD_IMMEDIATE_BYTE(new Load(NumberType.IMMEDIATE_BYTE)),
    LD_INDEXED_ADDRESS(new Load(NumberType.INDEXED_ADDRESS)),
    LD_REGISTERS(new Load(NumberType.REGISTER_VALUE)),
    LD_REGISTER_16_BIT(new Load(NumberType.REGISTER_16_BIT)),
    LD_ADDRESS_FROM_REGISTER_16_BIT(new Load(NumberType.ADDRESS_FROM_16BIT_REGISTER)),
//    LD_FROM_16_BIT(new Register16BitInstruction("LOAD", 0xF1)),

//    ADD(new Load<>(NumberType.IMMEDIATE_BYTE),"ld", "^r([0-9]+)"),
    STORE_IMMEDIATE_ADDRESS(new Store(NumberType.ADDRESS_POINTED_BY_16)),
    STORE_INDEXED_ADDRESS(new Store(NumberType.INDEXED_ADDRESS)),

    INC(new RegisterRowInstruction("INC", 0x08)),
    DEC(new RegisterRowInstruction("DEC", 0x18)),
    POP(new RegisterRowInstruction("POP", 0x88)),
    PUSH(new RegisterRowInstruction("PUSH", 0x98)),

    INC_16(new Register16BitInstruction("INC", 0xF4)),
    DEC_16(new Register16BitInstruction("DEC", 0xF5)),

    JMP(new ConditionalAddressPointer("JMP", NumberType.ADDRESS_POINTED_BY_16, 0x02, 0x03, 0x04)),
    CALL(new ConditionalAddressPointer("CALL", NumberType.ADDRESS_POINTED_BY_16, 0x70)),
    RET(new SingleByteConditional("RET", 0x71, 0xA9, 0xA8)),

    ROL(new AddressPointer("ROL", 0x72)),

    // Assembly helpers
    STRING(new AssemblyString()),
    WORD(new AssemblyWord()),


    ;

    Pattern[] patterns;
    Instruction instruction;
    OpCode(Instruction instruction){
        this.instruction = instruction;
//        patterns = new Pattern[regex.length+1];
//
//        patterns[0] = Pattern.compile(instruction.getOpcode());
//        for (int i = 0; i < regex.length; i++) {
//            patterns[i+1] = Pattern.compile(regex[i]);
//        }
    }

//    /**
//     * OpCode that only takes a register as it's only parameter.
//     * @param instruction
//     */
//    OpCode(RegisterRowInstruction instruction){
//        this(instruction, "^r([0-9]+)");
//    }
//
//    OpCode(AddressPointer instruction){
//        this(instruction, "^.*$");
//    }

    public boolean matches(String... tokens){
        return instruction.regexMatch(tokens);
//        if(tokens.length != patterns.length) return false;
//
//        for (int i = 0; i < tokens.length; i++) {
//            Matcher matcher = patterns[i].matcher(tokens[i]);
//            if(!matcher.matches()){
//                return false;
//            } else {
//                matcher.reset();
//                if(matcher.find()){
//                    for(int m = 0; m < matcher.groupCount(); m++) {
//                        System.out.println("Pattern matches: " + matcher.group(m));
//                    }
//                }
//            }
//        }
//
//        return true;
    }

    public Instruction getInstruction() {
        return instruction;
    }
}
