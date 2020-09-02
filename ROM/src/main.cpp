#include <Arduino.h>
#include <rom.h>

// functions
void onClock();
void dataWrite(byte val);
void addressWrite(byte val);
uint16_t readAddress();
// void startRead();

////////////////////////////////////////////////////////////
// Address Pins - MSB First
const uint8_t pins[9] = {12,11,10,9,8,7,6,5,4};

// Data Pins - MSB First
const uint8_t data[8] = {PIN4, PIN3, A0, A1, A2, A3, A4, A5};

// RW Pin - If set HIGH begins a ROM read. Gets set LOW once data has been read and is on the output line.
const int OE = PIN2;

// 74HC595 PINS
// pin connected to ST_CP of 74HC595
const byte PIN_LATCH = A1;
// pin connected to SH_CP of 74HC595
const byte PIN_CLOCK = A0;
// pin connected to DS of 74HC595
const byte PIN_DATA = 13;
///////////////////////////////////////////////////////////

// Current address.
uint16_t address;


void setup() {
  // Setup Output enable pin as input
  pinMode(OE, INPUT);

  // Set all address pins as inputs
  for (uint8_t pin : pins) {
    pinMode(pin, INPUT);
  }

  // Set all data pins as outputs
  for (uint8_t pin : data) {
    pinMode(pin, OUTPUT);
  }

  // Set shift register pins
  pinMode(PIN_LATCH, OUTPUT);
  pinMode(PIN_CLOCK, OUTPUT);
  pinMode(PIN_DATA, OUTPUT);

  // attachInterrupt(digitalPinToInterrupt(RW), startRead, RISING);

  // Reset output pins.
  dataWrite(0);
}

// Used to check if data has been output on OE change
volatile boolean hasChanged = false;

void loop() {
  if(digitalRead(OE) == HIGH) {
    if(!hasChanged) {
      hasChanged = true;
      dataWrite(0);
    }
    return;
  }

  // Read current address.
  uint16_t readAdr = readAddress();

  // If address hasn't changed return since output is the same.
  if(readAdr == address && !hasChanged) return;
  address = readAdr;
  hasChanged = false;

  // Write from rom the current data at the given address.
  dataWrite(rom[address]);
}


uint16_t readAddress(){
  	uint16_t value = 0;
    uint8_t i;

    for (i = 0; i < sizeof(pins); ++i) {
      value |= digitalRead(pins[i]) << (i);
    }
    return value;
};

void dataWrite(byte val){
  // MSB First
  // for(uint8_t i=0; i < sizeof(data); i++){
  //   digitalWrite(data[i], !!(val & (1 << ((sizeof(data)-1) - i))));
  // }

  // take the latchPin low so 
  // the LEDs don't change while you're sending in bits:
  digitalWrite(PIN_LATCH, LOW);
  // shift out the bits:
  shiftOut(PIN_DATA, PIN_CLOCK, LSBFIRST, val);  

  //take the latch pin high so the LEDs will light up:
  digitalWrite(PIN_LATCH, HIGH);
}

// void startRead() {
//   pinMode(RW, OUTPUT);
//   digitalWrite(RW, HIGH);

//   // Read current address.
//   uint16_t readAdr = readAddress();

//   // If address hasn't changed return since output is the same.
//   if(readAdr == address && !hasChanged){
//     digitalWrite(RW, LOW);
//     pinMode(RW, INPUT);
//     return;
//   } 
//   address = readAdr;
//   hasChanged = false;

//   // Write from rom the current data at the given address.
//   dataWrite(rom.data[address]);

//   // Set RW pin to low and as an input to wait for new 
//   digitalWrite(RW, LOW);
//   pinMode(RW, INPUT);
// }