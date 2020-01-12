#include "SerialBridge.h"

extern uint8_t target;
extern uint16_t command;

void initSerialBridge() {
    Serial_Init(9600, false);
    Serial_CreateStream(NULL);

    sei();
    UCSR1B |= (1 << RXCIE1);
}

void parseLine(char *line) {
	char t[8];
	char c[16];
  sscanf(line, "%s %s", t, c);
	if (strcasecmp(t, "Button") == 0) {
		target = Button;
	} else if (strcasecmp(t, "LX") == 0) {
		target = LX;
	} else if (strcasecmp(t, "LY") == 0) {
		target = LY;
	} else if (strcasecmp(t, "RX") == 0) {
		target = RX;
	} else if (strcasecmp(t, "RY") == 0) {
		target = RY;
	} else if (strcasecmp(t, "HAT") == 0) {
		target = HAT;
	} else {
		target = RELEASE;
	}
	if (strcasecmp(c, "Y") == 0) {
		command = SWITCH_Y;
	} else if (strcasecmp(c, "B") == 0) {
		command = SWITCH_B;
	} else if (strcasecmp(c, "A") == 0) {
		command = SWITCH_A;
	} else if (strcasecmp(c, "X") == 0) {
		command = SWITCH_X;
	} else if (strcasecmp(c, "L") == 0) {
		command = SWITCH_L;
	} else if (strcasecmp(c, "R") == 0) {
		command = SWITCH_R;
	} else if (strcasecmp(c, "ZL") == 0) {
		command = SWITCH_ZL;
	} else if (strcasecmp(c, "ZR") == 0) {
		command = SWITCH_ZR;
	} else if (strcasecmp(c, "SELECT") == 0) {
		command = SWITCH_SELECT;
	} else if (strcasecmp(c, "START") == 0) {
		command = SWITCH_START;
	} else if (strcasecmp(c, "LCLICK") == 0) {
		command = SWITCH_LCLICK;
	} else if (strcasecmp(c, "RCLICK") == 0) {
		command = SWITCH_RCLICK;
	} else if (strcasecmp(c, "HOME") == 0) {
		command = SWITCH_HOME;
	} else if (strcasecmp(c, "CAPTURE") == 0) {
		command = SWITCH_CAPTURE;
	} else if (strcasecmp(c, "RELEASE") == 0) {
		command = SWITCH_RELEASE;
	} else if (strcasecmp(c, "MIN") == 0) {
		command = STICK_MIN;
	} else if (strcasecmp(c, "MAX") == 0) {
		command = STICK_MAX;
	} else if (strcasecmp(c, "TOP") == 0) {
		command = HAT_TOP;
	} else if (strcasecmp(c, "TOP_RIGHT") == 0) {
		command = HAT_TOP_RIGHT;
	} else if (strcasecmp(c, "RIGHT") == 0) {
		command = HAT_RIGHT;
	} else if (strcasecmp(c, "BOTTOM_RIGHT") == 0) {
		command = HAT_BOTTOM_RIGHT;
	} else if (strcasecmp(c, "BOTTOM") == 0) {
		command = HAT_BOTTOM;
	} else if (strcasecmp(c, "BOTTOM_LEFT") == 0) {
		command = HAT_BOTTOM_LEFT;
	} else if (strcasecmp(c, "LEFT") == 0) {
		command = HAT_LEFT;
	} else if (strcasecmp(c, "TOP_LEFT") == 0) {
		command = HAT_TOP_LEFT;
	} else if (strcasecmp(c, "CENTER") == 0) {
		if (target == HAT) {
			command = HAT_CENTER;
		} else {
			command = STICK_CENTER;
		}
	} else {
		target = RELEASE;
	}
}

#define MAX_BUFFER 32
char b[MAX_BUFFER];
uint8_t l = 0;
ISR(USART1_RX_vect) {
	char c = fgetc(stdin);
	if (Serial_IsSendReady()) {
		printf("%c", c);
	}
	if (c == '\r') {
		parseLine(b);
		l = 0;
		memset(b, 0, sizeof(b));
	} else if (c != '\n' && l < MAX_BUFFER) {
		b[l++] = c;
	}
}