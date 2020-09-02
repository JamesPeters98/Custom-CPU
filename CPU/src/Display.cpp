#include <Display.h>

bool needsWrite = true;

uint8_t Display::readOffset(uint16_t offset) {
    if(offset == 0) return toByte(displayInfo);
    return VRAM[offset-1];
}

void Display::writeOffset(uint16_t offset, uint8_t byte) {
    if(offset == 0){
        fromByte(byte, displayInfo);
        displayInfoCheck();
    } else {
        if(VRAM[offset-1] != byte){
            VRAM[offset-1] = byte;
            needsWrite =true;
        }
    }
}

void Display::displayInfoCheck() {
    if(displayInfo[0] == true && needsWrite){
        lcd.getLCD().setCursor(0,0);
        lcd.getLCD().println(VRAM);
        needsWrite = false;
        // for(int i=0; i < 16; i++){
        //     lcd.getLCD().print(VRAM[i]);
        // }
        // lcd.getLCD().setCursor(0,1);
        // for(int i=16; i < 32; i++){
        //     lcd.getLCD().print(VRAM[i]);
        // }
    }
}