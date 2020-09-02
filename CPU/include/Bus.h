#ifndef CPU_MEMORY_H
#include <Memory.h>
#endif

#ifndef   CPU_BUS_H
#define   CPU_BUS_H

class Bus {

    private:
        Memory* getMemory(uint16_t addr);
    public:
        uint8_t readMemory(uint16_t addr);
        void writeMemory(uint16_t addr, uint8_t data);
};

#endif
