package ms.ralph.switch.serial.controller.serial

import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext
import ms.ralph.switch.serial.controller.serial.macro.MacroData
import ms.ralph.switch.serial.controller.serial.macro.MacroThread
import org.slf4j.Logger
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class SerialController(private val log: Logger) {
    private val coroutineContext = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    private var activeClient: SerialClient? = null

    private var macroThread: MacroThread? = null

    suspend fun sendOnce(portId: String, value: String) = withContext(coroutineContext) {
        log.debug("Send to '$portId': $value")
        stopMacro()
        ensureClient(portId)
        activeClient?.send(value)
    }

    suspend fun sendOnceToCurrentPort(value: String) = withContext(coroutineContext) {
        log.debug("Send to current port: $value")
        activeClient?.send(value)
    }

    suspend fun setMacro(portId: String, macro: String) = withContext(coroutineContext) {
        log.debug("Macro Start")
        val macroData = MacroData.parse(macro)
        ensureClient(portId)
        startMacro(macroData)
    }

    private fun startMacro(macroData: MacroData) {
        stopMacro()
        macroThread = MacroThread(log, this, macroData)
        macroThread?.start()
    }

    private fun stopMacro() {
        macroThread?.requestStop()
        macroThread?.join()
    }

    private fun ensureClient(portId: String) {
        if (activeClient == null) {
            createNewClient(portId)
            return
        }
        if (activeClient?.portId != portId) {
            createNewClient(portId)
            return
        }
    }

    private fun createNewClient(portId: String) {
        activeClient?.close()
        activeClient = SerialClient(portId)
        activeClient?.connect()
    }
}
