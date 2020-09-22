package assembler.types.labels;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum LabelType {

    // Order of enum here matters.
    // Higher priority labels should be at the start of the declaration.
    START(new StartLabel(), ".start:"),
    CONSTANT(Constant.class, "^([A-Z_])([A-Z_0-9]*)$", "^=$", "^(.*)"), // Matches an all caps keyword with underscores.
    CODE(CodeLabel.class, "^(.*):"), //Matches for labels in the format label:
    ;

    Pattern[] patterns;
    Label label;
    Class<? extends Label> labelClass;

    /**
     * This constructs a static label for unique labels such as .start and .data
     * @param label
     * @param regex
     */
    LabelType(Label label, String... regex){
        this.label = label;
        setup(regex);
    }


    LabelType(Class<? extends Label> labelClass, String... regex) {
        this.labelClass = labelClass;
        setup(regex);
    }

    private void setup(String... regex) {
        patterns = new Pattern[regex.length];

        for (int i = 0; i < regex.length; i++) {
            patterns[i] = Pattern.compile(regex[i]);
        }
    }

    public boolean matches(String... tokens){
        if(tokens.length != patterns.length) return false;

        for (int i = 0; i < tokens.length; i++) {
            Matcher matcher = patterns[i].matcher(tokens[i]);
            if(!matcher.matches()){
                System.out.println("Matcher for "+tokens[i]+" doesn't match");
                return false;
            } else {
                matcher.reset();
                if(matcher.find()){
                    for(int m = 0; m < matcher.groupCount(); m++) {
                        System.out.println("Label matches: " + matcher.group(m));
                    }
                }
            }
        }

        return true;
    }

    public Pattern[] getPatterns() {
        return patterns;
    }

    public Label getLabel() {
        if(label == null){
            try {
                return labelClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return label;
    }
}
