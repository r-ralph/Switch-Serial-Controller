#pragma once

#include <stdint.h>

#define HAT_CENTER 0x08
#define STICK_CENTER 128

typedef struct {
    uint8_t a;
    uint8_t b;
    uint8_t x;
    uint8_t y;
    uint8_t l;
    uint8_t r;
    uint8_t zl;
    uint8_t zr;
    uint8_t select;
    uint8_t start;
    uint8_t l_stick;
    uint8_t r_stick;
    uint8_t home;
    uint8_t capture;
	uint8_t hat;
	uint8_t lx;
	uint8_t ly;
	uint8_t rx;
	uint8_t ry;
} ControllerState_t;