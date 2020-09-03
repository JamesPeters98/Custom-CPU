#ifndef CPU_MEMORY_H
#include <Memory.h>
#endif

class Ram : public Memory {

    public:
        // 60 KibiBytes of RAM
        Ram() : Memory(0x0F00, 0x1300){
            RAM = new uint8_t[getSize()];
        }

        uint8_t * RAM;

        uint8_t readOffset(uint16_t offset) override;
        void writeOffset(uint16_t offset, uint8_t byte) override;
};
