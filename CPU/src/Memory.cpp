#include <Memory.h>

Memory::Memory(uint16_t startAddr, uint16_t endAddr) {
    this->startAddr = startAddr;
    this->endAddr = endAddr;
}

Memory::Memory() {
    this->startAddr = 0;
    this->endAddr = 0;
}

uint16_t Memory::getSize() {
    return endAddr - startAddr + 1;
}

uint16_t Memory::getStartAddr() {
    return startAddr;
}

uint16_t Memory::getEndAddr() {
    return endAddr;
}

// Read/Write methods
uint8_t Memory::read(uint16_t addr) {
    pushAddressToBus(addr);
    return readOffset(addr - startAddr);
}

void Memory::write(uint16_t addr, uint8_t byte) {
    pushAddressToBus(addr);
    writeOffset(addr - startAddr, byte);
}