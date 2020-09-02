package assembler.types.values;

import assembler.Main;
import org.joou.UByte;

/**
 * Represents a byte that has yet to be determined.
 * E.g a label which address isn't known until the compiler reaches it.
 */
public class UndeterminedByte {

    protected UByte uByte;
    protected String label; // Label associated with this byte

    public UndeterminedByte(String label) {
        this.label = label;
    }

    public UByte getByte() {
        if(uByte == null) throw new RuntimeException("Tried to access value of undetermined byte before it has been defined. Label = "+getLabel()+" on line: "+ Main.ROM_LINE_NUMBER);
        return uByte;
    }

    public void setByte(UByte value) {
        uByte = value;
    }

    public String getLabel() {
        return label;
    }
}
