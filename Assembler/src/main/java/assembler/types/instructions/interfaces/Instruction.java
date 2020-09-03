package assembler.types.instructions.interfaces;

import org.joou.UShort;
import assembler.types.values.UndeterminedByte;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Instruction {

    protected void validateInstruction(String[] tokens){
        String str = tokens[0];
        if(!str.equals(getOpcode())){
            throw new IllegalArgumentException(getOpcode()+" instruction called but token = "+str);
        }
    }

    public abstract UShort compileInstruction(HashMap<UShort, UndeterminedByte> bytes, UShort currentPos, String... tokens);

    public int getRegisterIndex(String registerStr) {
        try {
            return Integer.parseInt(registerStr.replaceAll("r", ""));
        } catch (NumberFormatException e){
            e.printStackTrace();
            System.exit(-1);
        }
        return -1;
    }

    public boolean regexMatch(String... tokens){
        System.out.println("Checking Instruction: "+getOpcode());
        List<Pattern[]> patternsList = getPatternArray();
        for(Pattern[] patterns : patternsList) {
            if (tokens.length != patterns.length){
                System.out.println("Different amount of tokens found! token length: "+tokens.length+" pattern length: "+patterns.length);
                continue;
            }

            boolean isValid = true;
            for (int i = 0; i < tokens.length; i++) {
                Matcher matcher = patterns[i].matcher(tokens[i]);
                if (!matcher.matches()) {
                    System.out.println("Matcher didn't match token: "+tokens[i]+" to pattern: "+matcher.toString());
                    isValid = false;
                    break;
                } else {
                    matcher.reset();
                    if (matcher.find()) {
                        for (int m = 0; m < matcher.groupCount(); m++) {
                            System.out.println("Pattern matches: " + matcher.group(m));
                        }
                    }
                }
            }
            return isValid;
        }

        return false;
    }

    /**
     * @return A List containing an array of patterns to match tokens too.
     * Most classes only require a list with 1 element.
     */
    protected abstract List<Pattern[]> getPatternArray();

    // Returns the token for this opcode e.g 'NOP'
    public abstract String getOpcode();


    public Pattern[] createPatternFromRegex(String... regex){
        Pattern[] patterns = new Pattern[regex.length+1];

        patterns[0] = Pattern.compile(getOpcode());
        for (int i = 0; i < regex.length; i++) {
            patterns[i+1] = Pattern.compile(regex[i]);
        }

        return patterns;
    }

    public List<Pattern[]> createListPatternFromRegex(String... regex){
        List<Pattern[]> list = new ArrayList<>();
        list.add(createPatternFromRegex(regex));
        return list;
    }
}
