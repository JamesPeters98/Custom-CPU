package assembler.types.labels;

import org.joou.UShort;

public interface Label {

    UShort onLabelFound(UShort currentPos, String... tokens);

    /**
     *
     * @return
     */
    UShort getLabelAddress();
}
