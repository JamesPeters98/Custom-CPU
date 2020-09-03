; This is a test comment!

VRAM = $401 ; Start of line 0 on LCD.
STRING = hello-world

.start:
    LOAD r0 #$00        ; This loads 0 into register 0.
    LOAD r1 #%10000000  ; This loads 80 into register 1.
    STORE r0 $400       ; This loads 0 into the display register
    PUSH r1             ; Pushes value of register 1 onto the stack.

    ;; PRINT STRING
    LOAD r2 #$00        ; This stores 0 into register 2. This is the starting position on the LCD.
    LOAD r16,STRING     ; Load address for start of string into 16bit register.
    CALL print_string   ; jump to subroutine to print string.

    ;; PRINT STRING
    LOAD r2 #40                 ; This stores 40 into register 2. This is the second line on the LCD.
    LOAD r16,second-line        ; Load address for start of string into 16bit register.
    CALL print_string           ; jump to subroutine to print string.

end:
    POP r1              ; Pops the value stored on the stack into register 1.
    STORE r1 $400       ; This pushes %10000000 to 0x400 to tell the CPU to push VRAM to the display.
    HALT

print_string:
    ; This section writes each character to VRAM which is located at 0x401 -> 0x450 (80 bytes - 40 bytes per line.) only 16 bytes visible per line.
    LOAD r0,r16         ; Loads character from String into register 0.
    RET Z               ; Jump if Loaded a Zero (End of String)
    STORE VRAM,r2       ; Stores character into VRAM.
    INC r16             ; Increment register r16 to select next character.
    INC r2              ; Increment position in VRAM.
    JMP print_string    ; loop until reach null termination character.

;; Strings
hello-world:
    String "Hello James!" ; This creates a null-terminated ASCII String.

second-line:
    String "Test String!" ;

