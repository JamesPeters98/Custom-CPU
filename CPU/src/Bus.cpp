#ifndef CPU_BUS_H
#include <Bus.h>
#endif

#include <Rom.h>
#include <Ram.h>
#include <Display.h>

// void Bus::addMemoryBanks(Memory* memory[], size_t len) {
//     uint16_t memorySize = 0;
//     for(uint16_t i = 0; i < len; i++) {
//         Memory* mem = memory[i];
//         memorySize = memorySize + mem->getSize();
//     }
//     Serial.println("Total mem size: "+String(memorySize));

//     handler = new Memory*[memorySize];

//     for(uint16_t i = 0; i < len; i++) {
//         Memory* mem = memory[i];
//         Serial.println("Mem size: "+String(mem->getSize()));
//         for(uint16_t addr = mem->getStartAddr(); addr <= mem->getEndAddr(); addr++) {
//             Serial.println("Loop: "+String(addr));
//             handler[addr] = mem;
//             Serial.println("Assigning mem pointer: "+String(i));
//             Serial.println("Mem size: "+String(mem->getSize()));
//         }
//     }
// }

const int memoryBanks = 3;
Memory* memory[memoryBanks] = {new Rom(), new Ram(), new Display()};
Memory defaultMem;

Memory* Bus::getMemory(uint16_t addr) {
    for(Memory* mem : memory) {
        if((addr >= mem->getStartAddr()) && (addr <= mem->getEndAddr()) ) return mem;
    }
    return &defaultMem;
}

uint8_t Bus::readMemory(uint16_t addr) {
    return getMemory(addr)->read(addr);
}

void Bus::writeMemory(uint16_t addr, uint8_t byte) {
    getMemory(addr)->write(addr, byte);
}