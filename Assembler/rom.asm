; This is a test comment!

VRAM = $401 ; Start of line 0 on LCD.
STRING = hello-world

VALUE = $0F00 ; Start of RAM - 2 bytes long
VALUE_2 = $0F01 ; Second byte of value - 2 bytes long

MOD10 = $0F02 ; 2 bytes long
MOD10_2 = $0F03 ; 2 bytes long

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

    ;; Call LCD update
    POP r1              ; Pops the value stored on the stack into register 1.
    STORE r1 $400       ; This pushes %10000000 to 0x400 to tell the CPU to push VRAM to the display.

    ;; Convert number to decimal
    LOAD r16,number         ; Loads number into register
    ;CALL convert_number     ; Calls convert number function

end:
    HALT

print_string:
    ; This section writes each character to VRAM which is located at 0x401 -> 0x450 (80 bytes - 40 bytes per line.) only 16 bytes visible per line.
    LOAD r0,r16         ; Loads character from String into register 0.
    RET Z               ; Jump if Loaded a Zero (End of String)
    STORE VRAM,r2       ; Stores character into VRAM.
    INC r16             ; Increment register r16 to select next character.
    INC r2              ; Increment position in VRAM.
    JMP print_string    ; loop until reach null termination character.

convert_number:
    LOAD r0,r16
    STORE r0 VALUE
    INC r16
    STORE r0 VALUE_2

    LOAD r0 #0
    STORE r0 MOD10
    STORE r0 MOD10_2

    ; Rotates the byte
    ROL VALUE
    ROL VALUE_2
    ROL MOD10
    ROL MOD10_2

;; Strings
hello-world:
    String "Hello James!" ; This creates a null-terminated ASCII String.

second-line:
    String "Test String!" ;

number:
    .word 1234

