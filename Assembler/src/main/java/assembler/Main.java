package assembler;

import org.joou.UShort;
import assembler.output.CArrayOutput;
import assembler.exceptions.InvalidOpCodeException;
import assembler.types.values.UndeterminedByte;
import assembler.types.instructions.OpCode;
import assembler.types.labels.LabelType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Main {

    public static final UShort ROM_SIZE = UShort.valueOf(1024);

    public static int ROM_LINE_NUMBER = 0;

    static int romPos = 0x00; // Start ROM at 00.
    static HashMap<UShort, UndeterminedByte> bytes;
    static UShort currentPos = UShort.valueOf(romPos);

    static String outputPath;
    static String CoutputPath;

    public static void main(String[] args) {
        String filePath = args[0];
        outputPath = args[1];
        CoutputPath = args[2];
        File file = new File(filePath);

        bytes = new HashMap<>();

        System.out.println("File Path: "+filePath);
        System.out.println("File name: "+file.getName());

        try {
            List<String> lines = Files.readAllLines(file.toPath());
            parseFile(lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void parseFile(List<String> lines){
        for (String line : lines) {
            if(line.startsWith(";")){
                // Is a comment line ignore
                System.out.println("Found a comment: "+line);
                continue;
            }
            if(!(line.startsWith(" ") || line.isEmpty())) {
                System.out.println("Found label!");

                String[] tokens = getTokens(line);
                System.out.println("Label Tokens: "+Arrays.toString(tokens));

                for (LabelType value : LabelType.values()) {
                    if (value.matches(tokens)){
                        System.out.println("Token matches label: "+value.toString());
                        currentPos = value.getLabel().onLabelFound(currentPos, tokens);
                        break;
                    }
                }
            } else {
                // Is an instruction
                String[] tokens = getTokens(line);
                System.out.println("Tokens: "+ Arrays.toString(tokens));
                System.out.println("Tokens length "+ tokens.length);
                if(tokens.length == 1 && tokens[0].trim().isEmpty()) continue;

                boolean foundOpCode = false;
                for (OpCode value : OpCode.values()) {
                    if(value.matches(tokens)){
                        System.out.println("Tokens match code: "+value.toString());
                        currentPos = value.getInstruction().compileInstruction(bytes, currentPos, tokens);
                        foundOpCode = true;
                        break;
                    }
                }
                if(!foundOpCode) throw new InvalidOpCodeException(tokens);
            }
            ROM_LINE_NUMBER++;
        }

        bytes.forEach((uShort, uByte) -> {
            System.out.println(Integer.toHexString(uShort.intValue())+" : "+Integer.toHexString(uByte.getByte().intValue()));
        });

        byte[] byteArray = new byte[ROM_SIZE.intValue()];
        for (UShort i = UShort.MIN; i.compareTo(ROM_SIZE) < 0; i = i.add(1)) {
            UndeterminedByte val = bytes.get(i);
            if (val != null) {
                byteArray[i.intValue()] = val.getByte().byteValue();
            } else {
                byteArray[i.intValue()] = 0;
            }
        }

        try {
            Files.write(Paths.get(outputPath), byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            CArrayOutput.output(byteArray, CoutputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // See https://stackoverflow.com/a/9584469
    private static final String REMOVE_WHITESPACE = "\\s+(?=((\\\\[\\\\\"]|[^\\\\\"])*\"(\\\\[\\\\\"]|[^\\\\\"])*\")*(\\\\[\\\\\"]|[^\\\\\"])*$)";

    private static String[] getTokens(String line) {
        String trimmed = line.replaceAll(REMOVE_WHITESPACE, " ").trim();

        int commentStart = trimmed.indexOf(";");
        if(commentStart != -1) trimmed = trimmed.substring(0, commentStart);
        String[] tokens = trimmed.split(REMOVE_WHITESPACE);
        return tokens;
    }


}
