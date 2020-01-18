package ms.ralph.switch.serial.controller.view.main

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.routing.Routing
import io.ktor.routing.get
import kotlinx.css.Align
import kotlinx.css.BorderStyle
import kotlinx.css.BoxSizing
import kotlinx.css.CSSBuilder
import kotlinx.css.Color
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.JustifyContent
import kotlinx.css.em
import kotlinx.css.flex
import kotlinx.css.padding
import kotlinx.css.pct
import kotlinx.css.properties.borderBottom
import kotlinx.css.properties.borderRight
import kotlinx.css.px
import kotlinx.css.vh
import kotlinx.html.BODY
import kotlinx.html.HEAD
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.head
import kotlinx.html.id
import kotlinx.html.iframe
import kotlinx.html.link
import kotlinx.html.script
import kotlinx.html.textArea
import kotlinx.html.textInput
import ms.ralph.switch.serial.controller.respondCss
import ms.ralph.switch.serial.controller.serial.SerialUtil
import ms.ralph.switch.serial.controller.view.flexPanel
import ms.ralph.switch.serial.controller.view.flexPanelBody
import ms.ralph.switch.serial.controller.view.flexPanelHeader
import ms.ralph.switch.serial.controller.view.includeCommonCss
import ms.ralph.switch.serial.controller.view.includePanelViewCss
import ms.ralph.switch.serial.controller.view.includeResetCss
import ms.ralph.switch.serial.controller.view.util.mdc.MdcSelectItem
import ms.ralph.switch.serial.controller.view.util.mdc.includeMdcSwitchLeftLabelCss
import ms.ralph.switch.serial.controller.view.util.mdc.mdcOutlinedSelect
import ms.ralph.switch.serial.controller.view.util.mdc.mdcSwitchLeftLabel
import ms.ralph.switch.serial.controller.view.util.mdc.mdcUnelevatedButton

fun Routing.mainRoute() {
    get("/") {
        call.respondMainView()
    }
    get("/styles.css") {
        call.respondCss {
            mainCss()
        }
    }
}

private suspend fun ApplicationCall.respondMainView() = respondHtml {
    head {
        mainHead()
    }
    body {
        mainBody()
    }
}

private fun HEAD.mainHead() {
    link(rel = "stylesheet", href = "/styles.css", type = "text/css")
    link(
        rel = "stylesheet",
        href = "https://unpkg.com/material-components-web@latest/dist/material-components-web.min.css",
        type = "text/css"
    )
    script {
        src = "https://unpkg.com/material-components-web@latest/dist/material-components-web.min.js"
    }
    script {
        src = "/static/js/index.js"
    }
}

private fun CSSBuilder.mainCss() {
    includeResetCss()
    includeCommonCss()
    includePanelViewCss()
    includeMdcSwitchLeftLabelCss()
    "#container" {
        height = 100.vh
        display = Display.flex
        backgroundColor = Color("#fafafa")
    }
    "#container>div:not(:last-child)" {
        borderRight(1.px, BorderStyle.solid, Color.gray)
    }
    "#control_panel" {
        flex(3.0, 1.0, 0.px)
        minWidth = 200.px
    }
    "#input_panel" {
        flex(7.0, 1.0, 0.px)
    }
    "#input_panel>div:not(:last-child)" {
        borderBottom(1.px, BorderStyle.solid, Color.gray)
    }
    "#gamepad_container" {
        display = Display.flex
        flexDirection = FlexDirection.column
        justifyContent = JustifyContent.center
        alignItems = Align.center
        flex(1.0, 1.0, 0.px)
    }
    "#gamepad_container_frame" {
        display = Display.block
        width = 100.pct
        height = 100.pct
        put("object-fit", "contain")
    }
    "#gamepad_raw_value_input" {
        width = 100.pct
        boxSizing = BoxSizing.borderBox
    }
    "#macro_text_input" {
        flex(1.0, 1.0, 0.px)
        padding(0.5.em)
        border = "none"
        put("resize", "none")
    }
    ".panel_item" {
        paddingLeft = 16.px
        paddingRight = 16.px
        paddingTop = 16.px
        paddingBottom = 16.px
    }
    ".panel_item_switch" {
        paddingLeft = 24.px
        paddingRight = 24.px
    }
    ".panel_item_width" {
        width = 100.pct
    }
}

private fun BODY.mainBody() = div {
    id = "container"
    flexPanel {
        id = "control_panel"
        flexPanelHeader("Control")
        flexPanelBody {
            div("panel_item") {
                mdcOutlinedSelect(
                    id = "panel_control_select_port",
                    widthClass = "panel_item_width",
                    label = "Serial Port",
                    items = SerialUtil.getAvailablePorts().map { MdcSelectItem(it, it) }
                )
            }
            div("panel_item panel_item_switch") {
                mdcSwitchLeftLabel(
                    id = "panel_control_switch_direct_send",
                    widthClass = "panel_item_width",
                    label = "Direct Send",
                    isSelected = false
                )
            }
            div("panel_item panel_item_switch") {
                mdcSwitchLeftLabel(
                    id = "panel_control_switch_hold_gamepad",
                    widthClass = "panel_item_width",
                    label = "Hold Gamepad Input",
                    isSelected = true
                )
            }
        }
    }
    flexPanel {
        id = "input_panel"
        flexPanel {
            flexPanelHeader("Gamepad") {
                mdcUnelevatedButton("button_send", "Send")
            }
            flexPanelBody("vertical_flex_container") {
                div {
                    id = "gamepad_container"
                    iframe {
                        id = "gamepad_container_frame"
                        src = "frame/gamepad"
                    }
                }
                div {
                    id = "gamepad_raw_value_container"
                    textInput {
                        id = "gamepad_raw_value_input"
                        readonly = true
                    }
                }
            }
        }
        flexPanel {
            flexPanelHeader("Macro") {
                mdcUnelevatedButton("button_start_macro", "Start Macro")
            }
            flexPanelBody("vertical_flex_container") {
                id = "macro_text_input_container"
                textArea {
                    id = "macro_text_input"
                    placeholder = "# Write macro commands here"
                }
            }
        }
    }
}
