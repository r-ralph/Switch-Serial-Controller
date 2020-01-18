package ms.ralph.switch.serial.controller.serial

import purejavacomm.CommPortIdentifier
import purejavacomm.SerialPort

class SerialClient(
    val portId: String
) {

    private var serialPort: SerialPort? = null

    fun connect() {
        val commPortIdentifier = CommPortIdentifier.getPortIdentifier(portId)
        val serialPort = commPortIdentifier.open(javaClass.name, 1000) as SerialPort

        serialPort.setSerialPortParams(
            BAUD_RATE,
            SerialPort.DATABITS_8,
            SerialPort.STOPBITS_1,
            SerialPort.PARITY_NONE
        )
        serialPort.isDTR = false
        serialPort.isRTS = false
        serialPort.enableReceiveTimeout(1000)

        Thread {
            val buffer = ByteArray(100)
            while (true) {
                val readCount = serialPort.inputStream.read(buffer)
                if (readCount == 0) {
                    continue
                }
                if (readCount == -1) {
                    break
                }
            }
        }.start()
        this.serialPort = serialPort
    }

    fun send(text: String) {
        val sendText = "$text\r"
        serialPort?.outputStream?.write(sendText.toByteArray(Charsets.UTF_8))
        serialPort?.outputStream?.flush()
    }

    fun close() {
        serialPort?.inputStream?.close()
        serialPort?.outputStream?.close()
        serialPort?.removeEventListener()
        serialPort?.close()
        serialPort = null
    }

    companion object {
        private const val BAUD_RATE = 9600
    }
}
