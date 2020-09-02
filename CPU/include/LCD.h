#include <LiquidCrystal_I2C.h>

#ifndef CPU_LCD_H
#define CPU_LCD_H

class LCD
{
private:
    LiquidCrystal_I2C lcd;
public:
    void setup();

    // Maxium of 16 chars.
    void setLine(uint8_t row, String string);

    LiquidCrystal_I2C getLCD(){
        return lcd;
    }
};

#endif
