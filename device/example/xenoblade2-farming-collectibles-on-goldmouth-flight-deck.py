#!/usr/bin/env python3
import argparse
import serial
from time import sleep

parser = argparse.ArgumentParser()
parser.add_argument('port')
args = parser.parse_args()

def send(msg, duration=0):
    print(msg)
    ser.write(f'{msg}\r\n'.encode('utf-8'));
    sleep(duration)
    ser.write(b'RELEASE\r\n');

ser = serial.Serial(args.port, 9600)

send('Button A', 0.1)
sleep(1)
send('Button A', 0.1)
sleep(1)
send('Button A', 0.1)
sleep(3)

try:
    while True:
        sleep(1)
        send('Button X', 0.1)
        sleep(2)
        send('Button A', 0.1)
        sleep(1)
        send('Button A', 0.1)
        sleep(5)
        send('LY MIN', 2)
        send('RX MAX', 0.57)
        send('LY MIN', 3.5)
        send('Button A', 0.1)
        sleep(10)
        send('LX MAX', 0.6)
        send('LX MIN', 1.1)
except KeyboardInterrupt:
    send('RELEASE')
    ser.close()
