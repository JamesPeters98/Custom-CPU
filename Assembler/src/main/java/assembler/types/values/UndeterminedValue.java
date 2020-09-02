package assembler.types.values;

import org.joou.UByte;
import org.joou.UShort;

import java.util.HashMap;

public class UndeterminedValue {

    private static final HashMap<String, UndeterminedValue> undeterminedValues = new HashMap<>();

    public static UndeterminedValue getValue(String label){
        if(undeterminedValues.containsKey(label)) {
            return undeterminedValues.get(label);
        } else {
            UndeterminedValue val = new UndeterminedValue(label);
            undeterminedValues.put(label, val);
            return val;
        }
    }

    public static void setUndeterminedWord(String label, UShort word) {
        UndeterminedValue val = undeterminedValues.get(label);
        // Label could be defined before it has been called.
        if(val == null){
            val = new UndeterminedValue(label);
            undeterminedValues.put(label, val);
        }
        val.setWord(word);
    }

    private final UndeterminedByte lsb, msb;
    private String label;

    private UndeterminedValue(String label) {
        this.label = label;
        lsb = new UndeterminedByte(label);
        msb = new UndeterminedByte(label);
    }

    public void setWord(UShort word) {
        int wordInt = word.intValue();
        lsb.setByte(UByte.valueOf(wordInt & 0xFF));
        msb.setByte(UByte.valueOf(wordInt & 0xFF00));
    }

    public UndeterminedByte[] toSignificantBitArray() {
        return new UndeterminedByte[]{lsb, msb};
    }

    public UndeterminedByte getLSB() {
        return lsb;
    }

    public UndeterminedByte getMSB() {
        return msb;
    }

    public String getLabel() {
        return label;
    }
}
