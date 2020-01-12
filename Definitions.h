#pragma once

#include <stdint.h>

// Type Defines
// Enumeration for joystick buttons.
typedef enum {
	SWITCH_Y       = 0x01,
	SWITCH_B       = 0x02,
	SWITCH_A       = 0x04,
	SWITCH_X       = 0x08,
	SWITCH_L       = 0x10,
	SWITCH_R       = 0x20,
	SWITCH_ZL      = 0x40,
	SWITCH_ZR      = 0x80,
	SWITCH_SELECT  = 0x100,
	SWITCH_START   = 0x200,
	SWITCH_LCLICK  = 0x400,
	SWITCH_RCLICK  = 0x800,
	SWITCH_HOME    = 0x1000,
	SWITCH_CAPTURE = 0x2000,
  SWITCH_RELEASE = 0x00,
} JoystickButtons_t;

#define HAT_TOP          0x00
#define HAT_TOP_RIGHT    0x01
#define HAT_RIGHT        0x02
#define HAT_BOTTOM_RIGHT 0x03
#define HAT_BOTTOM       0x04
#define HAT_BOTTOM_LEFT  0x05
#define HAT_LEFT         0x06
#define HAT_TOP_LEFT     0x07
#define HAT_CENTER       0x08

#define STICK_MIN      0
#define STICK_CENTER 128
#define STICK_MAX 255

typedef enum {
	Button,
	LX,
	LY,
	RX,
	RY,
	HAT,
	RELEASE,
} Target_t;

// Joystick HID report structure. We have an input and an output.
typedef struct {
	uint16_t Button; // 16 buttons; see JoystickButtons_t for bit mapping
	uint8_t  HAT;    // HAT switch; one nibble w/ unused nibble
	uint8_t  LX;     // Left  Stick X
	uint8_t  LY;     // Left  Stick Y
	uint8_t  RX;     // Right Stick X
	uint8_t  RY;     // Right Stick Y
	uint8_t  VendorSpec;
} USB_JoystickReport_Input_t;

// The output is structured as a mirror of the input.
// This is based on initial observations of the Pokken Controller.
typedef struct {
	uint16_t Button; // 16 buttons; see JoystickButtons_t for bit mapping
	uint8_t  HAT;    // HAT switch; one nibble w/ unused nibble
	uint8_t  LX;     // Left  Stick X
	uint8_t  LY;     // Left  Stick Y
	uint8_t  RX;     // Right Stick X
	uint8_t  RY;     // Right Stick Y
} USB_JoystickReport_Output_t;