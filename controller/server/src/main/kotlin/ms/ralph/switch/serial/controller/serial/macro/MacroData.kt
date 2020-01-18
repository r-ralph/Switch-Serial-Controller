package ms.ralph.switch.serial.controller.serial.macro

import ms.ralph.switch.serial.controller.serial.DeviceProtocol

data class MacroData(val commands: List<Command>) {

    sealed class Command {
        data class Wait(val waitTimeMillis: Long) : Command()
        data class ChangeState(val value: String) : Command()
        data class Label(val name: String) : Command()
        data class Jump(val targetLabel: String) : Command()
    }

    /**
     * A;B;X;Y;L;R;ZL;ZR;LCLICK;RCLICK;START;SELECT;HOME;CAPTURE;HAT@1;LSTICK@270,0.5;RSTICK@0,0
     * :label
     * goto label
     * wait 10
     */
    companion object {
        fun parse(rawText: String): MacroData {
            val commands = rawText.lineSequence()
                .filter(String::isNotBlank)
                .filter { !it.startsWith("#") }
                .map(String::trim)
                .map { it.toLowerCase() }
                .map { toCommand(it) }
                .toList()
            return MacroData(commands)
        }

        private fun toCommand(rawCommandText: String): Command = when {
            rawCommandText == "release" -> Command.ChangeState(DeviceProtocol.DEFAULT_DEVICE_TEXT)
            rawCommandText.startsWith("goto") -> {
                val args = rawCommandText.split(" ")
                check(args.size == 2)
                Command.Jump(args[1])
            }
            rawCommandText.startsWith("wait") -> {
                val args = rawCommandText.split(" ")
                check(args.size == 2)
                Command.Wait(args[1].toLong())
            }
            rawCommandText.startsWith(":") -> {
                val name = rawCommandText.substring(1)
                check(name.isNotEmpty())
                Command.Label(name)
            }
            else -> Command.ChangeState(parseDeviceText(rawCommandText))
        }

        fun parseDeviceText(rawCommandText: String): String {
            val buttonStateList = rawCommandText.split(";")
            val buttonA = buttonStateList.contains("a")
            val buttonB = buttonStateList.contains("b")
            val buttonX = buttonStateList.contains("x")
            val buttonY = buttonStateList.contains("y")
            val buttonL = buttonStateList.contains("l")
            val buttonR = buttonStateList.contains("r")
            val buttonZL = buttonStateList.contains("zl")
            val buttonZR = buttonStateList.contains("zr")
            val buttonLClick = buttonStateList.contains("lclick")
            val buttonRClick = buttonStateList.contains("rclick")
            val buttonStart = buttonStateList.contains("start")
            val buttonSelect = buttonStateList.contains("select")
            val buttonHome = buttonStateList.contains("home")
            val buttonCapture = buttonStateList.contains("capture")

            val hatRawText = buttonStateList.find { it.startsWith("hat") }?.split("@")?.getOrNull(1)
            val hat = hatRawText?.toInt() ?: 8

            val leftStickRawText = buttonStateList.find { it.startsWith("lstick") }?.split("@")?.getOrNull(1)
            val leftStickArgs = leftStickRawText?.split(",")
            val leftStickDegrees = leftStickArgs?.getOrNull(0)?.toFloat() ?: 0f
            val leftStickTilt = leftStickArgs?.getOrNull(1)?.toFloat() ?: 0f

            val rightStickRawText = buttonStateList.find { it.startsWith("rstick") }?.split("@")?.getOrNull(1)
            val rightStickArgs = rightStickRawText?.split(",")
            val rightStickDegrees = rightStickArgs?.getOrNull(0)?.toFloat() ?: 0f
            val rightStickTilt = rightStickArgs?.getOrNull(1)?.toFloat() ?: 0f

            return DeviceProtocol.toDeviceText(
                buttonA,
                buttonB,
                buttonX,
                buttonY,
                buttonL,
                buttonR,
                buttonZL,
                buttonZR,
                buttonSelect,
                buttonStart,
                buttonLClick,
                buttonRClick,
                buttonHome,
                buttonCapture,
                hat,
                leftStickDegrees,
                leftStickTilt,
                rightStickDegrees,
                rightStickTilt
            )
        }
    }
}
