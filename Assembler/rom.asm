; This is a test comment!

VRAM = $401 ; Start of line 0 on LCD.

.start:
    LOAD r0 #$00        ; This loads 0 into register 0.
    LOAD r1 #%10000000  ; This loads 80 into register 1.
    LOAD r2 #$00        ; This stores 0 into register 2.
    STORE r0 $400       ; This loads 0 into the display register

loop:
    ; This section writes each character to VRAM which is located at 0x401 -> 0x450 (80 bytes - 40 bytes per line.)
    LOAD hello-world,r2        ; Loads character from String "hello-world:"
    JZ end              ; Jump if Loaded a Zero (End of String)
    STORE VRAM,r2       ; Stores character into VRAM.
    INC r2              ; Increment register r2
    JUMP loop

hello-world:
    String "Hello James!" ; This creates a null-terminated ASCII String.

end:
    STORE r1 $400 ; This pushes %10000000 to 0x400 to tell the CPU to push VRAM to the display.
    HALT