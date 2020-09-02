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
void dec(uint8_t opcode);

////////////////////////////
// REGISTERS
uint16_t cpu_register[8];
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
    runCpuCycle(val);
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

void dec(uint8_t opcode){
  uint8_t firstInstruction = 0x18;
  uint8_t registerIndex = opcode - firstInstruction;
  cpu_register[registerIndex] += -1;

  // Set flags
  ZERO = cpu_register[registerIndex] == 0; // Set Zero flag.

  PC++;
}