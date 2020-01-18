package ms.ralph.switch.serial.controller.serial

import purejavacomm.CommPortIdentifier

object SerialUtil {
    fun getAvailablePorts(): List<String> = CommPortIdentifier.getPortIdentifiers().toList().map { it.name }
}
