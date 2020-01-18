package ms.ralph.switch.serial.controller.serial.macro

import kotlinx.coroutines.runBlocking
import ms.ralph.switch.serial.controller.serial.SerialController
import org.slf4j.Logger
import java.util.concurrent.atomic.AtomicBoolean

class MacroThread(
    private val log: Logger,
    private val serialController: SerialController,
    private val macroData: MacroData
) : Thread() {

    private val isRunningMacro: AtomicBoolean = AtomicBoolean(true)
    private var currentIndex = 0
    private val labels: Map<String, Int> = macroData.commands
        .filterIsInstance<MacroData.Command.Label>()
        .mapIndexed { index, label -> label.name to index }
        .toMap()

    override fun run() {
        while (currentIndex < macroData.commands.size && isRunningMacro.get()) {
            val command = macroData.commands[currentIndex]
            log.debug("Process Macro command $command")
            when (command) {
                is MacroData.Command.Wait -> sleep(command.waitTimeMillis)
                is MacroData.Command.ChangeState -> runBlocking { serialController.sendOnceToCurrentPort(command.value) }
                is MacroData.Command.Label -> Unit
                is MacroData.Command.Jump -> Unit
            }

            if (command is MacroData.Command.Jump) {
                currentIndex = labels[command.targetLabel] ?: currentIndex + 1
                continue
            }
            currentIndex++
        }
        log.debug("End Macro")
    }

    fun requestStop() {
        isRunningMacro.set(false)
    }
}
