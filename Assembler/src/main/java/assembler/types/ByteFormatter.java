package assembler.types;

import assembler.types.labels.Constant;
import assembler.types.values.DeterminedByte;
import assembler.types.values.UndeterminedByte;
import assembler.types.values.UndeterminedValue;

import java.util.regex.Pattern;

public class ByteFormatter {

    private static final String HEX = "$";
    private static final String BINARY = "%";
    private static final String ASCII = "\"";
    private static final String CONSTANT = "^([A-Z_]*)$";
    private static final String LABEL = "^[A-Za-z.]";
    // Decimal has no prefix.

    private static final Pattern label = Pattern.compile(LABEL);
    private static final Pattern constant = Pattern.compile(CONSTANT);

    public static DeterminedByte getByte(String value){
        value = Constant.convert(value);
        value = value.replaceAll("#", "");
        System.out.println("Number: "+value);
        if (value.startsWith(HEX)){
            return DeterminedByte.valueOf(Integer.parseUnsignedInt(value.replaceAll("\\"+HEX, ""), 16));
        } else if (value.startsWith(BINARY)) {
            return DeterminedByte.valueOf(Integer.parseUnsignedInt(value.replaceAll(BINARY, ""), 2));
        } else if (value.startsWith(ASCII) && value.endsWith(ASCII)) {
            String charVal = value.substring(1, value.length() - 1);
            if(charVal.length() > 1){
                throw new IllegalArgumentException("Char cannot be more than one character: "+charVal);
            }
            if(charVal.length() == 0) return DeterminedByte.MIN;
            char character = charVal.toCharArray()[0];
            return DeterminedByte.valueOf(character);
        } else {
            return DeterminedByte.valueOf(Integer.parseUnsignedInt(value));
        }
    }

    /**
     * Returns two bytes derived from the word provided.
     * Little endian format, so [LSB, MSB]
     * @param number word being converted.
     * @return
     */
    public static UndeterminedByte[] getWord(String number) {
        number = Constant.convert(number);
        number = number.replaceAll("#", "");
        System.out.println("Number: "+number);
        System.out.println("Is label: "+number.matches(LABEL));
        if (number.startsWith(HEX)){
            return fromWord(Integer.parseUnsignedInt(number.replaceAll("\\"+HEX, ""), 16));
        }
        else if (number.startsWith(BINARY)) {
            return fromWord(Integer.parseUnsignedInt(number.replaceAll(BINARY, ""), 2));
        }
        else if (label.matcher(number).find()){
            // Returns undetermined byte array for the provided label.
            return UndeterminedValue.getValue(number).toSignificantBitArray();
        }
        else {
            return fromWord(Integer.parseUnsignedInt(number));
        }
    }

    private static DeterminedByte[] fromWord(int word) {
        DeterminedByte[] arr = new DeterminedByte[2];
        arr[0] = DeterminedByte.valueOf(word & 0xFF);
        arr[1] = DeterminedByte.valueOf((word & 0xFF00) >> 8);
        return arr;
    }
}
