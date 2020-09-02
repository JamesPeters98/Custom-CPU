#ifndef CPU_MEMORY_H
#include <Memory.h>
#endif

class Ram : public Memory {

    public:
        // 4KB of RAM
        Ram() : Memory(0x800, 0xFFFF){
            RAM = new uint8_t[getSize()];
        }

        uint8_t* RAM;

        uint8_t readOffset(uint16_t offset) override;
        void writeOffset(uint16_t offset, uint8_t byte) override;
};
