package assembler.types;

public enum NumberType {
    // The immediate byte to the right of instruction
    IMMEDIATE_BYTE,

    // This value is the memory location pointed to by the next two bytes (Little endian)
    ADDRESS_POINTED_BY_16,

    // This value is the memory location pointed to by the next two bytes + the value in the given register.
    INDEXED_ADDRESS;
}
