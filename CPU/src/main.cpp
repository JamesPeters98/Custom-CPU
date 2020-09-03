#include <Arduino.h>
#include <MemoryFree.h>

#ifndef CPU_MEMORY_H
#include <Memory.h>
#endif

#ifndef CPU_PINS_H
#include <Pins.h>
#endif

#ifndef CPU_H
#include <CPU.h>
#endif

// Functions
void runCpuCycle(uint8_t code);
void onCycleStart();
void onCycleEnd();

// Opcodes
void load(uint8_t opcode);
void load_absolute_index(uint8_t opcode);
void load_registers(uint8_t opcode);
void load_into_16bit(uint8_t opcode);
void load_address_from_16bit(uint8_t opcode);
void store(uint8_t opcode);
void store_absolute_index(uint8_t opcode);
void nop(uint8_t opcode);
void stop(uint8_t opcode);
void jump(uint8_t opcode);
void jump_not_zero(uint8_t opcode);
void jump_if_zero(uint8_t opcode);
void add(uint8_t opcode);
void subtract(uint8_t opcode);
void inc(uint8_t opcode);
void inc_16bit(uint8_t opcode);
void dec(uint8_t opcode);
void dec_16bit(uint8_t opcode);

void push(uint8_t opcode);
void pop(uint8_t opcode);
void call(uint8_t opcode);
void ret();
void ret_if_zero(boolean isZero);

////////////////////////////
// REGISTERS
uint16_t cpu_register[8];
uint16_t register_16bit;
/////////////////////////////

/////////////////////////////
// FLAGS
bool ZERO = false;
/////////////////////////////

// Currently this is needed to read data from the arduino rom
// Will be able to be removed once a real ROM can be used.
const unsigned long rom_delay = 400L; // microseconds.

// PROGRAM COUNTER
uint16_t PC;
// STACK POINTER
uint16_t STACK_POINTER;
bool isHalted = false;

// the setup function runs once when you press reset or power the board
void setup() {
  Serial.begin(9600);

  // Set all address pins as outputs
  for (uint8_t pin : pins) {
    pinMode(pin, OUTPUT);
  }

  // Set all data pins as inputs
  for (uint8_t pin : data) {
    pinMode(pin, INPUT);
  }

  // Clock pin is set as output (This is used to measure the clock speed of the CPU)
  pinMode(CLK_PIN, OUTPUT);

  // Entry point in ROM is 0xA0 (160) or (0b10100000)
  PC = 0xA0;

  // Initialise Stack pointer to 0xFFFE
  STACK_POINTER = 0x12FF;

  // Currently using the i2c LCD library. 
  lcd.setup();
  lcd.getLCD().clear(); // Possibly will remove this in the future and rely on the ROM to clear the LCD.
}

// the loop function runs over and over again forever
void loop() {
  onCycleStart();
  if(!isHalted){
    // Reads data currently on the data bus
    uint8_t val = bus.readMemory(PC);
    // Serial.println("PC: "+String(PC, HEX));
    runCpuCycle(val);
    // Serial.println("r16 = "+String(register_16bit, HEX));
    // Serial.println("r0 = "+String(cpu_register[0], HEX));
    // Serial.println("r1 = "+String(cpu_register[1], HEX));
  }
  onCycleEnd();
}

void onCycleStart(){
  digitalWrite(CLK_PIN, HIGH);
}

void onCycleEnd(){
  digitalWrite(CLK_PIN, LOW);
}

// Called when the cpu cycles, code is the current data on the bus
void runCpuCycle(uint8_t code) {
  switch (code)
  {
  case 0x00:
    nop(code);
    break;
  
  case 0x01:
    stop(code);
    break;

  case 0x02:
    jump(code);
    break;

  case 0x03:
    jump_not_zero(code);
    break;

  case 0x04:
    jump_if_zero(code);
    break;
  
  case 0x10: case 0x11: case 0x12: case 0x13: case 0x14: case 0x15: case 0x16: case 0x17:
    load(code);
    break;

  case 0x20: case 0x21: case 0x22: case 0x23: case 0x24: case 0x25: case 0x26: case 0x27:
    store(code);
    break;

  case 0x80: case 0x81: case 0x82: case 0x83: case 0x84: case 0x85: case 0x86: case 0x87:
    add(code);
    break;

  case 0x90: case 0x91: case 0x92: case 0x93: case 0x94: case 0x95: case 0x96: case 0x97:
    subtract(code);
    break;

  case 0xC0: case 0xC1: case 0xC2: case 0xC3: case 0xC4: case 0xC5: case 0xC6: case 0xC7:
    load_absolute_index(code);
    break;

  case 0xD0: case 0xD1: case 0xD2: case 0xD3: case 0xD4: case 0xD5: case 0xD6: case 0xD7:
    store_absolute_index(code);
    break;

    
  case 0x08: case 0x09: case 0x0A: case 0x0B: case 0x0C: case 0x0D: case 0x0E: case 0x0F:
    inc(code);
    break;

  case 0x18: case 0x19: case 0x1A: case 0x1B: case 0x1C: case 0x1D: case 0x1E: case 0x1F:
    dec(code);
    break;

  case 0x88: case 0x89: case 0x8A: case 0x8B: case 0x8C: case 0x8D: case 0x8E: case 0x8F:
    pop(code);
    break;

  case 0x98: case 0x99: case 0x9A: case 0x9B: case 0x9C: case 0x9D: case 0x9E: case 0x9F:
    push(code);
    break;

  case 0xE0: case 0xE1: case 0xE2: case 0xE3: case 0xE4: case 0xE5: case 0xE6: case 0xE7:
    load_address_from_16bit(code);
    break;

  case 0xF0:
    load_into_16bit(code);
    break;

  case 0xF4:
    inc_16bit(code);
    break;
  
  case 0xF5:
    dec_16bit(code);
    break;

  case 0x70:
    call(code);
    break;

  case 0x71:
    ret();
    break;

  case 0xA8:
    ret_if_zero(true);
    break;
  
  case 0xA9:
    ret_if_zero(false);
    break;
  
  default:
    Serial.println("No instruction for opcode: 0x"+String(code, HEX));
    break;
  }
}

// CPU INSTRUCTIONS

void load(uint8_t opcode){
  uint8_t firstInstruction = 0x10;
  uint8_t registerIndex = opcode - firstInstruction;

  onCycleEnd();
  PC++;
  uint8_t byte = bus.readMemory(PC);

  // Set flags
  ZERO = byte == 0; // Set Zero flag.

  cpu_register[registerIndex] = byte;
  onCycleStart();
  PC++;
}

void load_absolute_index(uint8_t opcode){
  uint8_t firstInstruction = 0xC0;
  uint8_t registerIndex = opcode - firstInstruction;

  // Find LSB and MSB of address.
  onCycleEnd();
  onCycleStart();
  PC++;
  uint8_t LSB = bus.readMemory(PC);
  onCycleEnd();
  PC++;
  uint8_t MSB = bus.readMemory(PC);
  onCycleStart();

  // Load value from the given register and add it to the calculated address.
  uint8_t value = cpu_register[registerIndex];
  uint16_t addr = word(MSB, LSB) + value;
  
  // Read byte for that address and store into register 0.
  uint8_t byte = bus.readMemory(addr);
  cpu_register[0] = byte;

  // Set flags
  ZERO = byte == 0; // Set Zero flag.

  PC++;
}

void load_registers(uint8_t opcode){
  uint8_t firstInstruction = 0x30;

  uint8_t offset = opcode - firstInstruction;
  // Shift right 3 divides by 8 and rounds down.
  uint8_t registerIndex = offset >> 3;
  // Shift that register left by 3 to multiply by 8 and subtract that from the offset.
  uint8_t registerSelect = offset - (registerIndex << 8);

  cpu_register[registerIndex] = cpu_register[registerSelect];

  ZERO = cpu_register[registerIndex] == 0;

  PC++;
}

void load_into_16bit(uint8_t opcode){
  // Find LSB and MSB of address.
  onCycleStart();
  PC++;
  uint8_t LSB = bus.readMemory(PC);
  onCycleEnd();
  onCycleStart();
  PC++;
  uint8_t MSB = bus.readMemory(PC);
  onCycleStart();

  // Load value from LSB and MSB.
  uint16_t byte = word(MSB, LSB);
  
  // Puts 16 bit value into register.
  register_16bit = byte;

  // Set flags
  ZERO = byte == 0; // Set Zero flag.

  PC++;
}

void load_address_from_16bit(uint8_t opcode){
  uint8_t firstInstruction = 0xE0;
  uint8_t registerIndex = opcode - firstInstruction;

  uint8_t value = bus.readMemory(register_16bit);
  cpu_register[registerIndex] = value;

  ZERO = value == 0;

  PC++;
}

void store(uint8_t opcode){
  uint8_t firstInstruction = 0x20;
  uint8_t registerIndex = opcode - firstInstruction;

  onCycleEnd();
  onCycleStart();
  PC++;
  uint8_t LSB = bus.readMemory(PC);
  onCycleEnd();
  PC++;
  uint8_t MSB = bus.readMemory(PC);
  onCycleStart();

  uint8_t value = cpu_register[registerIndex];
  uint16_t addr = word(MSB, LSB);
  bus.writeMemory(addr, value);

  // Set flags
  ZERO = value == 0; // Set Zero flag.
  
  PC++;
}

void store_absolute_index(uint8_t opcode){
  uint8_t firstInstruction = 0xD0;
  uint8_t registerIndex = opcode - firstInstruction;

  onCycleEnd();
  onCycleStart();
  PC++;
  uint8_t LSB = bus.readMemory(PC);
  onCycleEnd();
  PC++;
  uint8_t MSB = bus.readMemory(PC);
  onCycleStart();

  uint8_t value = cpu_register[0];
  uint16_t addr = word(MSB, LSB) + cpu_register[registerIndex];
  bus.writeMemory(addr, value);

  // Set flags
  ZERO = value == 0; // Set Zero flag.
  
  PC++;
}

void nop(uint8_t opcode){
  // Do nothing!
  PC++;
}

void stop(uint8_t opcode){
  Serial.println("Halting CPU!");
  isHalted = true;

  // for(uint16_t i = 0x401; i < 0x450; i++){
  //   uint8_t vram = bus.readMemory(i);
  //   Serial.println("VRAM: "+String(i, HEX)+" = "+String(vram, HEX));
  // }

  // Interrupts still need to be implemented  
}

void jump(uint8_t opcode){
  onCycleEnd();
  onCycleStart();
  PC++;
  uint8_t LSB = bus.readMemory(PC);
  onCycleEnd();
  PC++;
  uint8_t MSB = bus.readMemory(PC);
  onCycleStart();
  uint16_t addr = word(MSB, LSB);
  PC = addr;
}

void jump_not_zero(uint8_t opcode) {
  onCycleEnd();
  onCycleStart();
  PC++;
  uint8_t LSB = bus.readMemory(PC);
  onCycleEnd();
  PC++;
  uint8_t MSB = bus.readMemory(PC);
  onCycleStart();
  if(!ZERO){
    uint16_t addr = word(MSB, LSB);
    PC = addr;
  } else { 
    PC++;
  }
}

void jump_if_zero(uint8_t opcode) {
  onCycleEnd();
  onCycleStart();
  PC++;
  uint8_t LSB = bus.readMemory(PC);
  onCycleEnd();
  PC++;
  uint8_t MSB = bus.readMemory(PC);
  onCycleStart();
  if(ZERO){
    uint16_t addr = word(MSB, LSB);
    PC = addr;
  } else { 
    PC++;
  }
}

void add(uint8_t opcode){
  uint8_t firstInstruction = 0x80;
  uint8_t registerIndex = opcode - firstInstruction;
  cpu_register[0] = cpu_register[0] + cpu_register[registerIndex];

  // Set flags
  ZERO = cpu_register[0] == 0; // Set Zero flag.

  PC++;
}

void subtract(uint8_t opcode){
  uint8_t firstInstruction = 0x90;
  uint8_t registerIndex = opcode - firstInstruction;
  cpu_register[0] = cpu_register[0] - cpu_register[registerIndex];

  // Set flags
  ZERO = cpu_register[0] == 0; // Set Zero flag.

  PC++;
}

void inc(uint8_t opcode){
  uint8_t firstInstruction = 0x08;
  uint8_t registerIndex = opcode - firstInstruction;
  cpu_register[registerIndex] += 1;

  // Set flags
  ZERO = cpu_register[registerIndex] == 0; // Set Zero flag.

  PC++;
}

void inc_16bit(uint8_t opcode){
  register_16bit++;

  // Set flags
  ZERO = register_16bit == 0; // Set Zero flag.

  PC++;
}

void dec(uint8_t opcode){
  uint8_t firstInstruction = 0x18;
  uint8_t registerIndex = opcode - firstInstruction;
  cpu_register[registerIndex] += -1;

  // Set flags
  ZERO = cpu_register[registerIndex] == 0; // Set Zero flag.

  PC++;
}

void dec_16bit(uint8_t opcode){
  register_16bit--;

  // Set flags
  ZERO = register_16bit == 0; // Set Zero flag.

  PC++;
}

void push(uint8_t opcode){
  uint8_t firstInstruction = 0x98;
  uint8_t registerIndex = opcode - firstInstruction;

  bus.writeMemory(STACK_POINTER, cpu_register[registerIndex]);
  STACK_POINTER--;

  PC++;
}

void pop(uint8_t opcode){
  uint8_t firstInstruction = 0x98;
  uint8_t registerIndex = opcode - firstInstruction;

  STACK_POINTER++;
  cpu_register[registerIndex] = bus.readMemory(STACK_POINTER);

  PC++;
}

void call(uint8_t opcode){

  onCycleStart();
  PC++;
  uint8_t LSB = bus.readMemory(PC);
  onCycleEnd();
  PC++;
  uint8_t MSB = bus.readMemory(PC);
  onCycleStart();

  // Serial.println("SP: "+String(STACK_POINTER, HEX));
  bus.writeMemory(STACK_POINTER, lowByte(PC));
  STACK_POINTER--;
  onCycleEnd();

  onCycleStart();
  // Serial.println("SP: "+String(STACK_POINTER, HEX));
  bus.writeMemory(STACK_POINTER, highByte(PC));
  STACK_POINTER--;
  onCycleEnd();

  // Serial.println("SP: "+String(STACK_POINTER, HEX));
  // Serial.println("Wrote PC to RAM: high byte: "+String(highByte(PC), HEX)+" low byte: "+String(lowByte(PC), HEX));
  // Serial.println("From stack: high byte: "+String(bus.readMemory(STACK_POINTER+1), HEX)+" low byte: "+String(bus.readMemory(STACK_POINTER+2), HEX));

  uint16_t addr = word(MSB, LSB);
  PC = addr;
}

void ret(){
  STACK_POINTER++;
  uint8_t MSB = bus.readMemory(STACK_POINTER);
  onCycleEnd();

  onCycleStart();
  STACK_POINTER++;
  uint8_t LSB = bus.readMemory(STACK_POINTER);
  onCycleEnd();

  onCycleStart();
  PC = word(MSB, LSB) + 1; // +1 to move to next instruction
}

void ret_if_zero(boolean isZero){
  if(ZERO == isZero) ret();
  else PC++;
}

