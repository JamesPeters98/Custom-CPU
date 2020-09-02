package assembler.exceptions;

import java.util.Arrays;

public class InvalidOpCodeException extends RuntimeException {

    public InvalidOpCodeException(String[] tokens){
        super("Invalid line found in assembly file, no opcode found for: "+ Arrays.toString(tokens));
    }
}
