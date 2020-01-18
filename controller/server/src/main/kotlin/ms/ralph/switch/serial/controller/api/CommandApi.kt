package ms.ralph.switch.serial.controller.api

import io.ktor.application.call
import io.ktor.application.log
import io.ktor.http.HttpStatusCode
import io.ktor.http.cio.websocket.CloseReason
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.close
import io.ktor.http.cio.websocket.readText
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.post
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.generateNonce
import io.ktor.websocket.webSocket
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import ms.ralph.switch.serial.controller.serial.SerialController
import ms.ralph.switch.serial.controller.serial.macro.MacroData
import java.util.concurrent.TimeUnit

@Serializable
data class CommandSendModel(
    val port: String,
    val value: String
)

@Serializable
data class CommandMacroModel(
    val port: String,
    val macro: String
)

@UseExperimental(KtorExperimentalAPI::class)
fun Routing.commandApiRoute(serialController: SerialController) {
    webSocket("/api/command/send/ws/") {
        val nonce = generateNonce()
        call.application.log.debug("WebSocket session opened: $nonce")
        val subject = PublishSubject.create<Pair<String, String>>()
        val disposable = subject.observeOn(Schedulers.newThread())
            .throttleLatest(8, TimeUnit.MILLISECONDS, true) // Poll rate is 125Hz
            .subscribe(
                { command ->
                    val port = command.first
                    val deviceText = MacroData.parseDeviceText(command.second.toLowerCase())
                    runBlocking {
                        serialController.sendOnce(port, deviceText)
                    }
                },
                { e ->
                    call.application.log.debug("Subject error: $e")
                    e.printStackTrace()
                }
            )
        for (frame in incoming) {
            when (frame) {
                is Frame.Text -> {
                    val args = frame.readText().split(";", limit = 3)
                    when (args[0]) {
                        "quit" -> close(CloseReason(CloseReason.Codes.NORMAL, "Quit"))
                        "send" -> {
                            subject.onNext(args[1] to args[2])
                        }
                    }
                }
            }
        }
        disposable.dispose()
        call.application.log.debug("WebSocket session closed: $nonce")
    }

    post("/api/command/send") {
        val model = call.receive<CommandSendModel>()
        val deviceText = MacroData.parseDeviceText(model.value.toLowerCase())
        serialController.sendOnce(model.port, deviceText)
        call.respond(HttpStatusCode.OK)
    }

    post("/api/command/macro") {
        val model = call.receive<CommandMacroModel>()
        serialController.setMacro(model.port, model.macro)
        call.respond(HttpStatusCode.OK)
    }
}
