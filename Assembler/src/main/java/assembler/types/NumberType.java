package assembler.types;

public enum NumberType {
    // The immediate byte to the right of instruction
    IMMEDIATE_BYTE,

    // This value is the memory location pointed to by the next two bytes (Little endian)
    ADDRESS_POINTED_BY_16,

    // This value is the memory location pointed to by the next two bytes + the value in the given register.
    INDEXED_ADDRESS,

    // This denotes that the instruction includes
    REGISTER_VALUE,

    // This inserts the immediate 16 bit address into the 16 bit register.
    REGISTER_16_BIT,

    // This loads the value pointed to by the 16 bit register into register 0.
    ADDRESS_FROM_16BIT_REGISTER
    ;
}
