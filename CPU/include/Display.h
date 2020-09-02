#ifndef CPU_MEMORY_H
#include <Memory.h>
#endif

#ifndef CPU_H
#include <CPU.h>
#endif

class Display : public Memory {

    private:
        bool displayInfo[8];
        // bit 0 = UPDATE DISPLAY (When toggled causes the display to shift data from VRAM into the display.)
    public:
        // 4KB of RAM
        Display() : Memory(0x400, 0x450){
            // VRAM of 32 characters for the display.
            // First 16 bytes for top row, last 16 bytes for last row.
            // Additional byte needed for the NULL termination character.
            VRAM = new char[81];
            String initialData = "                                                                              ";
            initialData.toCharArray(VRAM, 81, 0);
        }

        char* VRAM;

        uint8_t readOffset(uint16_t offset) override;
        void writeOffset(uint16_t offset, uint8_t byte) override;

        void displayInfoCheck();
};
