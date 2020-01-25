# Switch Serial Controller - device

A device for manipulating Nintendo Switch via serial communication.

Based on [ebith's PoC](https://github.com/ebith/Switch-Fightstick).

## Requirement
- ATMega32U4 Board or see [shinyquagsire23/Switch-Fightstick's README](https://github.com/shinyquagsire23/Switch-Fightstick/blob/master/README.md)
- USB to serial adapter
- USB micro-b cable * 2

## How to build

### Make a device

TBD

### Build firmware and flush

```sh
brew tap osx-cross/avr
brew install avrdude avr-gcc

git clone --recursive https://github.com/r-ralph/Switch-Serial-Controller.git
cd Switch-Serial-Controller
cd device

make
avrdude -pm32u4 -cavr109 -D -P$(ls /dev/tty.usbmodem*) -b57600 -Uflash:w:SwitchSerialController.hex # need reset
```
