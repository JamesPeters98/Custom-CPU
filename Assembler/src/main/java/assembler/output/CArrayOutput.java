package assembler.output;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CArrayOutput {

    public static void output(byte[] byteArray, String outputFile) throws IOException {
        FileWriter fileWriter = new FileWriter(outputFile);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        printWriter.println("/* This is an auto-generated rom file created on "+ LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) +" */");
        printWriter.println("");

        printWriter.println("const uint8_t rom[1024] = {");
        for (int i = 0; i < byteArray.length/16; i++) {
            // loop each row of 16 bytes
            printWriter.print("\t");
            for (int b = 0; b < 16; b++){
                printWriter.print(String.format("0x%02X, ", byteArray[(i*16) + b]));
                if(b == 7) printWriter.print(" ");
            }
            printWriter.println("");
        }
        printWriter.println("};");

        printWriter.close();
    }
}
