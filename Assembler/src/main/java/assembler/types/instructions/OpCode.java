package assembler.types.instructions;

import assembler.types.NumberType;
import assembler.types.instructions.helpers.AssemblyString;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum OpCode {

    NOP(new NOP()),
    HALT(new Halt()),
    LD_IMMEDIATE_BYTE(new Load<>(NumberType.IMMEDIATE_BYTE), "^r([0-9]+)", "^\\#.*$"),
    LD_INDEXED_ADDRESS(new Load<>(NumberType.INDEXED_ADDRESS), "^(.*),r([0-9]+)$"),
//    ADD(new Load<>(NumberType.IMMEDIATE_BYTE),"ld", "^r([0-9]+)"),
    STORE_IMMEDIATE_ADDRESS(new Store(NumberType.ADDRESS_POINTED_BY_16),"^r([0-9]+)", "^.*$"),
    STORE_INDEXED_ADDRESS(new Store(NumberType.INDEXED_ADDRESS),"^(.*),r([0-9]+)$"),
    JUMP(new Jump<>(NumberType.ADDRESS_POINTED_BY_16, Jump.Type.UNCONDITIONAL), "^.*$"),
    JNZ(new Jump<>(NumberType.ADDRESS_POINTED_BY_16, Jump.Type.IF_NOT_ZERO), "^.*$"),
    JZ(new Jump<>(NumberType.ADDRESS_POINTED_BY_16, Jump.Type.IF_ZERO), "^.*$"),

    INC(new Inc(), "^r([0-9]+)"),
    DEC(new Dec(), "^r([0-9]+)"),

    // Assembly helpers
    STRING(new AssemblyString(), "^\\\"(.*)\\\"$"),


    ;

    Pattern[] patterns;
    Instruction instruction;
    OpCode(Instruction instruction, String... regex){
        this.instruction = instruction;
        patterns = new Pattern[regex.length+1];

        patterns[0] = Pattern.compile(instruction.getOpcode());
        for (int i = 0; i < regex.length; i++) {
            patterns[i+1] = Pattern.compile(regex[i]);
        }
    }

    public boolean matches(String... tokens){
        if(tokens.length != patterns.length) return false;

        for (int i = 0; i < tokens.length; i++) {
            Matcher matcher = patterns[i].matcher(tokens[i]);
            if(!matcher.matches()){
                return false;
            } else {
                matcher.reset();
                if(matcher.find()){
                    for(int m = 0; m < matcher.groupCount(); m++) {
                        System.out.println("Pattern matches: " + matcher.group(m));
                    }
                }
            }
        }

        return true;
    }

    public Instruction getInstruction() {
        return instruction;
    }
}
