#include <Rom.h>

uint8_t Rom::readOffset(uint16_t offset) {
    // Wait 400 microseconds for rom to output data.
    delayMicroseconds(400);

    // Read output
    uint8_t value = 0;
    uint8_t i;

    for (i = 0; i < sizeof(data); ++i) {
        value |= digitalRead(data[i]) << ((sizeof(data)-1) - i);
    }
    return value;
}

void Rom::writeOffset(uint16_t offset, uint8_t byte) {
    // ROM - no writes allowed!
}