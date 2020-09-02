#include "LCD.h"

#include <LiquidCrystal_I2C.h>
#include <Wire.h>

#define COLUMS           16
#define ROWS             2

// Setup LCD
void LCD::setup() {
    lcd = LiquidCrystal_I2C(PCF8574_ADDR_A21_A11_A01, 4, 5, 6, 16, 11, 12, 13, 14, POSITIVE);

    while (lcd.begin(COLUMS, ROWS) != 1) {
        Serial.println(F("PCF8574 is not connected or lcd pins declaration is wrong. Only pins numbers: 4,5,6,16,11,12,13,14 are legal."));
        delay(5000);   
    }
    // lcd.print(F("Welcome!"));    //(F()) saves string to flash & keeps dynamic memory free
}

void LCD::setLine(uint8_t line, String text) {
    lcd.setCursor(0, line);
    lcd.print(text);
}
