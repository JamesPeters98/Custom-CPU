#include <Ram.h>

uint8_t Ram::readOffset(uint16_t offset) {
    return RAM[offset];
}

void Ram::writeOffset(uint16_t offset, uint8_t byte) {
    RAM[offset] = byte;
}