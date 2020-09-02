package assembler.exceptions;

public class InvalidOpCodeOptionsException extends Exception {

    public InvalidOpCodeOptionsException(String line){
        super("Invalid line found in assembly file, arguments aren't valid for opcode: "+line);
    }
}
