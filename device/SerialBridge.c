#include "SerialBridge.h"

#define SERIAL_BUFFER_SIZE 13

extern ControllerState_t currentState;

// Buffer structure
// index | description
// 0-3   | Button bits in hex (apply padding on left with 0 or space)
// 4     | 8-direction HAT(0~8)
// 5-6   | Left Pad X
// 7-8   | Left Pad Y
// 9-10  | Right Pad X
// 11-12 | Right Pad Y


// index | target
// 0     | A
// 1     | B
// 2     | X
// 3     | Y
// 4     | L
// 5     | R
// 6     | ZL
// 7     | ZR
// 8     | SELECT
// 9     | START
// 10    | L-CLICK
// 11    | R-CLICK
// 12    | HOME
// 13    | CAPTURE
// 14    | UNUSED
// 15    | UNUSED
#define BIT_FLAG_BUTTON_A       (1<<0)
#define BIT_FLAG_BUTTON_B       (1<<1)
#define BIT_FLAG_BUTTON_X       (1<<2)
#define BIT_FLAG_BUTTON_Y       (1<<3)
#define BIT_FLAG_BUTTON_L       (1<<4)
#define BIT_FLAG_BUTTON_R       (1<<5)
#define BIT_FLAG_BUTTON_ZL      (1<<6)
#define BIT_FLAG_BUTTON_ZR      (1<<7)
#define BIT_FLAG_BUTTON_SELECT  (1<<8)
#define BIT_FLAG_BUTTON_START   (1<<9)
#define BIT_FLAG_BUTTON_L_STICK (1<<10)
#define BIT_FLAG_BUTTON_R_STICK (1<<11)
#define BIT_FLAG_BUTTON_HOME    (1<<12)
#define BIT_FLAG_BUTTON_CAPTURE (1<<13)
#define FLAGGED(v, f) (v) & (f) ? 1 : 0

char inputBuffer[SERIAL_BUFFER_SIZE];
uint8_t inputBufferPoint = 0;

void initSerialBridge() {
    Serial_Init(9600, false);
    Serial_CreateStream(NULL);

    sei();
    UCSR1B |= (1 << RXCIE1);
}

int clamp(int value, int min, int max) {
	if(value < min){
		return min;
	}else if(value > max){
		return max;
	} else {
		return value;
	}
}

void parseLine(char *line) {
	char raw_buttons[5];
	char raw_hat;
	char raw_lx[3];
	char raw_ly[3];
	char raw_rx[3];
	char raw_ry[3];
	uint16_t buttons;
	uint8_t hat;
	uint8_t lx;
	uint8_t ly;
	uint8_t rx;
	uint8_t ry;

	memcpy(raw_buttons, line, sizeof(char) * 4);
	raw_hat = line[4];
	memcpy(raw_lx, line + 5, sizeof(char) * 2);
	memcpy(raw_ly, line + 7, sizeof(char) * 2);
	memcpy(raw_rx, line + 9, sizeof(char) * 2);
	memcpy(raw_ry, line + 11, sizeof(char) * 2);

	raw_buttons[4] = 0;
	raw_lx[2] = 0;
	raw_ly[2] = 0;
	raw_rx[2] = 0;
	raw_ry[2] = 0;

	sscanf(raw_buttons, "%x", &buttons);
	hat = clamp(raw_hat - '0', 0, 8);
	lx = strtol(raw_lx, NULL, 16);
	ly = strtol(raw_ly, NULL, 16);
	rx = strtol(raw_rx, NULL, 16);
	ry = strtol(raw_ry, NULL, 16);

	currentState.a = FLAGGED(buttons, BIT_FLAG_BUTTON_A);
	currentState.b = FLAGGED(buttons, BIT_FLAG_BUTTON_B);
	currentState.x = FLAGGED(buttons, BIT_FLAG_BUTTON_X);
	currentState.y = FLAGGED(buttons, BIT_FLAG_BUTTON_Y);
	currentState.l = FLAGGED(buttons, BIT_FLAG_BUTTON_L);
	currentState.r = FLAGGED(buttons, BIT_FLAG_BUTTON_R);
	currentState.zl = FLAGGED(buttons, BIT_FLAG_BUTTON_ZL);
	currentState.zr = FLAGGED(buttons, BIT_FLAG_BUTTON_ZR);
	currentState.select = FLAGGED(buttons, BIT_FLAG_BUTTON_SELECT);
	currentState.start = FLAGGED(buttons, BIT_FLAG_BUTTON_START);
	currentState.l_stick = FLAGGED(buttons, BIT_FLAG_BUTTON_L_STICK);
	currentState.r_stick = FLAGGED(buttons, BIT_FLAG_BUTTON_R_STICK);
	currentState.home = FLAGGED(buttons, BIT_FLAG_BUTTON_HOME);
	currentState.capture = FLAGGED(buttons, BIT_FLAG_BUTTON_CAPTURE);
	currentState.hat = hat;
	currentState.lx = lx;
	currentState.ly = ly;
	currentState.rx = rx;
	currentState.ry = ry;
}

ISR(USART1_RX_vect) {
	char readChar = fgetc(stdin);
	// if (Serial_IsSendReady()) {
	// 	// echo
	// 	printf("%c", readChar);
	// }

	if (readChar == '\r') {
		parseLine(inputBuffer);
		inputBufferPoint = 0;
		memset(inputBuffer, 0, sizeof(inputBuffer));
	} else if (readChar != '\n' && inputBufferPoint < SERIAL_BUFFER_SIZE) {
		inputBuffer[inputBufferPoint++] = readChar;
	}
}