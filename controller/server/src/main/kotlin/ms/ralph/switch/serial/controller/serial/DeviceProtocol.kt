package ms.ralph.switch.serial.controller.serial

import java.util.BitSet
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin

object DeviceProtocol {

    const val DEFAULT_DEVICE_TEXT = "0000880808080"

    private const val BIT_INDEX_BUTTON_A = 0
    private const val BIT_INDEX_BUTTON_B = 1
    private const val BIT_INDEX_BUTTON_X = 2
    private const val BIT_INDEX_BUTTON_Y = 3
    private const val BIT_INDEX_BUTTON_L = 4
    private const val BIT_INDEX_BUTTON_R = 5
    private const val BIT_INDEX_BUTTON_ZL = 6
    private const val BIT_INDEX_BUTTON_ZR = 7
    private const val BIT_INDEX_BUTTON_SELECT = 8
    private const val BIT_INDEX_BUTTON_START = 9
    private const val BIT_INDEX_BUTTON_L_STICK = 10
    private const val BIT_INDEX_BUTTON_R_STICK = 11
    private const val BIT_INDEX_BUTTON_HOME = 12
    private const val BIT_INDEX_BUTTON_CAPTURE = 13

    fun toDeviceText(
        a: Boolean,
        b: Boolean,
        x: Boolean,
        y: Boolean,
        l: Boolean,
        r: Boolean,
        zl: Boolean,
        zr: Boolean,
        select: Boolean,
        start: Boolean,
        leftClick: Boolean,
        rightClick: Boolean,
        home: Boolean,
        capture: Boolean,
        hat: Int,
        leftStickDegree: Float,
        leftStickTilt: Float,
        rightStickDegree: Float,
        rightStickTilt: Float
    ): String {
        check(hat in 0..8)
        val buttons = toButtonsHexString(a, b, x, y, l, r, zl, zr, select, start, leftClick, rightClick, home, capture)
        val hatHex = hat.toString(16)
        val leftStick = toStickHexString(leftStickDegree, leftStickTilt)
        val rightStick = toStickHexString(rightStickDegree, rightStickTilt)
        val deviceText = buttons + hatHex + leftStick + rightStick
        check(deviceText.length == 13)
        return deviceText
    }

    private fun toStickHexString(
        stickDegree: Float,
        stickTilt: Float
    ): String {
        val rad = Math.toRadians(stickDegree.toDouble())
        val x = min(max(((cos(rad) * stickTilt + 1) * 128), 0.0), 255.0).roundToInt()
        val y = min(max(((-sin(rad) * stickTilt + 1) * 128), 0.0), 255.0).roundToInt()
        val hexX = x.toString(16).clampLength(2, '0')
        val hexY = y.toString(16).clampLength(2, '0')
        return hexX + hexY
    }

    private fun toButtonsHexString(
        a: Boolean,
        b: Boolean,
        x: Boolean,
        y: Boolean,
        l: Boolean,
        r: Boolean,
        zl: Boolean,
        zr: Boolean,
        select: Boolean,
        start: Boolean,
        leftClick: Boolean,
        rightClick: Boolean,
        home: Boolean,
        capture: Boolean
    ): String {
        val bitSet = BitSet(16)
        bitSet.set(BIT_INDEX_BUTTON_A, a)
        bitSet.set(BIT_INDEX_BUTTON_B, b)
        bitSet.set(BIT_INDEX_BUTTON_X, x)
        bitSet.set(BIT_INDEX_BUTTON_Y, y)
        bitSet.set(BIT_INDEX_BUTTON_L, l)
        bitSet.set(BIT_INDEX_BUTTON_R, r)
        bitSet.set(BIT_INDEX_BUTTON_ZL, zl)
        bitSet.set(BIT_INDEX_BUTTON_ZR, zr)
        bitSet.set(BIT_INDEX_BUTTON_SELECT, select)
        bitSet.set(BIT_INDEX_BUTTON_START, start)
        bitSet.set(BIT_INDEX_BUTTON_L_STICK, leftClick)
        bitSet.set(BIT_INDEX_BUTTON_R_STICK, rightClick)
        bitSet.set(BIT_INDEX_BUTTON_HOME, home)
        bitSet.set(BIT_INDEX_BUTTON_CAPTURE, capture)
        return bitSet.toHexString(4)
    }

    private fun BitSet.toHexString(lengthToAdjust: Int): String {
        val bitString = toLongArray().getOrNull(0)?.toString(16) ?: ""
        return bitString.clampLength(lengthToAdjust, '0')
    }

    private fun String.clampLength(lengthToClamp: Int, fillCharacter: Char): String = when {
        length == lengthToClamp -> this
        length > lengthToClamp -> substring(length - lengthToClamp)
        else -> fillCharacter.toString().repeat(lengthToClamp - length) + this
    }
}
