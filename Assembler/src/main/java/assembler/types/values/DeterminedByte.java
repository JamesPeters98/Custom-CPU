package assembler.types.values;

import org.joou.UByte;

public class DeterminedByte extends UndeterminedByte {


    public DeterminedByte(UByte uByte) {
        super("Defined Byte");
        this.uByte = uByte;
    }

    public static DeterminedByte valueOf(int uByte){
        return new DeterminedByte(UByte.valueOf(uByte));
    }

    public final static DeterminedByte MIN = new DeterminedByte(UByte.MIN);
    public final static DeterminedByte MAX = new DeterminedByte(UByte.MAX);

    @Override
    public UByte getByte() {
        return uByte;
    }

    @Override
    public void setByte(UByte value) {
        throw new RuntimeException("Cannot set value of already determined byte");
    }
}
