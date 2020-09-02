#include <Arduino.h>

#ifndef CPU_PINS_H
#include <Pins.h>
#endif

#ifndef   CPU_MEMORY_H
#define   CPU_MEMORY_H

class Memory
{
private:
    uint16_t startAddr, endAddr;

    virtual uint8_t readOffset(uint16_t byte) {
    // Default return value.
    return 0;
    }

    virtual void writeOffset(uint16_t offset, uint8_t byte) {
        // Do nothing
        Serial.println("Default write");
    }
public:
    Memory(uint16_t startAddr, uint16_t endAddr);
    Memory();

    uint8_t read(uint16_t addr);
    void write(uint16_t addr, uint8_t byte);

    uint16_t getSize();
    uint16_t getStartAddr();
    uint16_t getEndAddr();

    void pushAddressToBus(uint16_t addr) {
        // MSB First
        for(uint8_t i=0; i < sizeof(pins); i++){
            digitalWrite(pins[i], !!(addr & (1 << ((sizeof(pins)-1) - i))));
        }
    }

    void fromByte(uint8_t byte, bool b[8]) {
        for(int i=0; i < 8; i++){
            b[7-i] = (byte & (1<<i)) != 0;
        }
    }

    uint8_t toByte(bool b[8]) {
        uint8_t val = 0;
        for(int i=0; i < 8; i++) {
            if(b[i]) {
                val |= 1 << i;
            }
        }
        return val;
    }
};

#endif
