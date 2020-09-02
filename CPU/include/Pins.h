#include <Arduino.h>

#ifndef   CPU_PINS_H
#define   CPU_PINS_H

// Address pins
// Address Pins - MSB First
const uint8_t pins[8] = {38, 40, 42, 44, 46, 48, 50, 52};

// Data Pins - MSB First
const uint8_t data[8] = {39, 41, 43, 45, 47, 49, 51, 53};

// RW Pin from ROM.
const uint8_t RW_PIN = 2;

// CLK PIN
const uint8_t CLK_PIN = 4;

#endif