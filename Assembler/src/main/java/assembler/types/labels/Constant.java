package assembler.types.labels;

import org.joou.UShort;
import java.util.HashMap;

/**
 * Defines a Constant (Must be in all CAPS)
 */
public class Constant implements Label {

    private static final HashMap<String, String> constant = new HashMap<>();

    @Override
    public UShort onLabelFound(UShort currentPos, String... tokens) {
        String CONSTANT_LABEL = tokens[0];
        String value = tokens[2];

        // Put constant into hashmap to be substituted in later.
        constant.put(CONSTANT_LABEL, value);
        return currentPos;
    }

    @Override
    public UShort getLabelAddress() {
        return null;
    }

    public static String getConstantLabel(String label) {
        String s = constant.get(label);
        if(s == null) throw new RuntimeException("Label: "+label+" is not defined");
        return s;
    }

    /**
     * Converts a value from its constant value, will return the original value if it isnt a constant.
     * @param value
     * @return
     */
    public static String convert(String value){
        if(LabelType.CONSTANT.getPatterns()[0].matcher(value).find()){
            return getConstantLabel(value);
        }
        return value;
    }

}
