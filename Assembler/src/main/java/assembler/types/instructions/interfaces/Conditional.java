package assembler.types.instructions.interfaces;

public abstract class Conditional extends Instruction {

    public enum Type {
        UNCONDITIONAL,
        IF_ZERO,
        NOT_ZERO,
        ;
    }

    public Type getConditional(String token) {
        if(token.equals("NZ")) return Type.NOT_ZERO;
        if(token.equals("Z")) return Type.IF_ZERO;

        return Type.UNCONDITIONAL;
    }

    public abstract int getOpCode(Type type);
}
