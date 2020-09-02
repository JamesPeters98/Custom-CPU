#ifndef CPU_PINS_H
#include <Pins.h>
#endif

#ifndef CPU_MEMORY_H
#include <Memory.h>
#endif

class Rom : public Memory {
    public:
        // 4KB of ROM
        Rom() : Memory(0, 1023){}

        uint8_t readOffset(uint16_t offset) override;
        void writeOffset(uint16_t offset, uint8_t byte) override;
};