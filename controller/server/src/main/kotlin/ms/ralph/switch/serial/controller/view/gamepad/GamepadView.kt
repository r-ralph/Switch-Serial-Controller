package ms.ralph.switch.serial.controller.view.gamepad

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.routing.Routing
import io.ktor.routing.get
import kotlinx.css.Align
import kotlinx.css.BorderStyle
import kotlinx.css.CSSBuilder
import kotlinx.css.Color
import kotlinx.css.Cursor
import kotlinx.css.Display
import kotlinx.css.FlexBasis
import kotlinx.css.FlexDirection
import kotlinx.css.FlexWrap
import kotlinx.css.JustifyContent
import kotlinx.css.PointerEvents
import kotlinx.css.Position
import kotlinx.css.UserSelect
import kotlinx.css.body
import kotlinx.css.em
import kotlinx.css.html
import kotlinx.css.pct
import kotlinx.css.properties.border
import kotlinx.css.px
import kotlinx.css.quoted
import kotlinx.css.vh
import kotlinx.css.vw
import kotlinx.html.BODY
import kotlinx.html.HEAD
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.head
import kotlinx.html.id
import kotlinx.html.link
import kotlinx.html.p
import kotlinx.html.script
import ms.ralph.switch.serial.controller.respondCss
import ms.ralph.switch.serial.controller.view.includeResetCss

fun Routing.gamepadRoute() {
    get("/frame/gamepad") {
        call.respondGamepadView()
    }
    get("/frame/gamepad/styles.css") {
        call.respondCss {
            gamepadCss()
        }
    }
}

private suspend fun ApplicationCall.respondGamepadView() = respondHtml {
    head {
        gamepadHead()
    }
    body {
        gamepadBody()
    }
}

private fun HEAD.gamepadHead() {
    link(rel = "stylesheet", href = "/frame/gamepad/styles.css", type = "text/css")
    script {
        src = "/static/js/frame/gamepad.js"
    }
}

private fun CSSBuilder.gamepadCss() {
    includeResetCss()
    body {
        display = Display.flex
        flexDirection = FlexDirection.column
        justifyContent = JustifyContent.center
        alignItems = Align.center
    }
    "#gamepad_container" {
        position = Position.relative
    }
    media("(min-aspect-ratio: 4/3)") {
        html {
            fontSize = 5.vh
        }
        "#gamepad_container" {
            width = (100 * 4 / 3.0f).vh
            height = 100.vh
        }
    }
    media("(max-aspect-ratio: 4/3)") {
        html {
            fontSize = 3.75.vw
        }
        "#gamepad_container" {
            width = 100.vw
            height = 75.vw
        }
    }
    ".parts" {
        position = Position.absolute
        border(1.px, BorderStyle.solid, Color.darkGray)
        backgroundColor = Color("#e0e0e0")
    }
    ".button, .hat_button" {
        cursor = Cursor.pointer
    }
    ".button *, .hat_button *" {
        pointerEvents = PointerEvents.none
    }
    ".button_circle" {
        borderRadius = 100.pct
    }
    ".button_text" {
        display = Display.flex
        justifyContent = JustifyContent.center
        alignItems = Align.center
    }
    ".button_text > p" {
        color = Color.black
        fontSize = 1.em
        userSelect = UserSelect.none
    }
    ".button_normal" {
        width = 7.5.pct
        height = 10.pct
    }
    ".button_lr" {
        width = 25.pct
        height = 10.pct
    }
    ".button_mini" {
        width = 4.5.pct
        height = 6.pct
    }
    ".button_selected, .button_clicking, .hat_button_selected" {
        backgroundColor = Color("#aeaeae")
    }
    ".stick" {
        width = 18.pct
        height = 24.pct
    }
    ".hat" {
        width = 18.pct
        height = 24.pct
    }
    ".inner_circle" {
        position = Position.absolute
        put("width", "calc(100% - 2px)")
        put("height", "calc(100% - 2px)")
        border(1.px, BorderStyle.solid, Color.gray)
        borderRadius = 100.pct
    }
    ".reticle" {
        position = Position.absolute
        put("width", "calc(100% - 2px)")
        put("height", "calc(100% - 2px)")
        border(1.px, BorderStyle.solid, Color.gray)
        borderRadius = 100.pct
    }
    ".stick > .reticle::before" {
        content = "".quoted
        position = Position.absolute
        width = 2.px
        height = 100.pct
        put("left", "calc(50% - 1px)")
        backgroundColor = Color.gray
    }
    ".stick > .reticle::after" {
        content = "".quoted
        position = Position.absolute
        width = 100.pct
        height = 2.px
        put("top", "calc(50% - 1px)")
        backgroundColor = Color.gray
    }
    ".stick_cursor" {
        position = Position.absolute
        width = 12.px
        height = 12.px
        put("top", "calc(50% - 6px)")
        put("left", "calc(50% - 6px)")
        cursor = Cursor.move
    }
    ".stick_cursor_dragging" {
        cursor = Cursor.none
    }
    ".stick_cursor::before" {
        content = "".quoted
        position = Position.absolute
        width = 2.px
        height = 100.pct
        put("left", "calc(50% - 1px)")
        backgroundColor = Color.black
    }
    ".stick_cursor::after" {
        content = "".quoted
        position = Position.absolute
        width = 100.pct
        height = 2.px
        put("top", "calc(50% - 1px)")
        backgroundColor = Color.black
    }
    ".stick_actual" {
        position = Position.absolute
        width = 2.px
        height = 2.px
        put("top", "calc(50% - 1px)")
        put("left", "calc(50% - 1px)")
        borderRadius = 100.pct
        backgroundColor = Color.black
    }
    ".button_stick_reset" {
        cursor = Cursor.pointer
        width = 4.5.pct
        height = 6.pct
    }
    ".hat_direction" {
        flexBasis = FlexBasis("33%")
        flexGrow = 1.0
        width = 33.pct
        height = 33.pct
    }
    ".hat_boarder" {
        position = Position.absolute
        backgroundColor = Color.gray
    }
    ".hat_boarder_vertical" {
        width = 1.px
        height = 100.pct
    }
    ".hat_boarder_horizontal" {
        width = 100.pct
        height = 1.px
    }

    "#button_a" {
        top = 49.pct
        right = 10.pct
    }
    "#button_b" {
        top = 58.pct
        right = 17.pct
    }
    "#button_x" {
        top = 40.pct
        right = 17.pct
    }
    "#button_y" {
        top = 49.pct
        right = 24.pct
    }

    "#button_l" {
        top = 20.pct
        left = 10.pct
    }
    "#button_zl" {
        top = 5.pct
        left = 10.pct
    }
    "#button_r" {
        top = 20.pct
        right = 10.pct
    }
    "#button_zr" {
        top = 5.pct
        right = 10.pct
    }

    "#button_minus" {
        top = 40.pct
        left = 35.pct
    }
    "#button_plus" {
        top = 40.pct
        right = 35.pct
    }
    "#button_capture" {
        top = 53.pct
        left = 40.pct
    }
    "#button_capture::after" {
        content = "".quoted
        position = Position.absolute
        put("width", "calc(100% - 2px)")
        put("height", "calc(100% - 2px)")
        border(1.px, BorderStyle.solid, Color.darkGray)
        borderRadius = 100.pct
    }
    "#button_home" {
        top = 53.pct
        right = 40.pct
    }

    "#stick_left" {
        top = 40.pct
        left = 10.pct
    }
    "#button_l_stick" {
        top = 58.pct
        put("left", "calc(28.5% - 1px)")
    }
    "#button_stick_left_reset" {
        top = 58.pct
        put("left", "calc(5.5% - 1px)")
    }
    "#stick_right" {
        top = 71.pct
        right = 25.pct
    }
    "#button_r_stick" {
        top = 89.pct
        put("right", "calc(20.5% - 1px)")
    }
    "#button_stick_right_reset" {
        top = 89.pct
        put("right", "calc(43.5% - 1px)")
    }
    "#hat" {
        top = 71.pct
        left = 25.pct
        display = Display.flex
        flexWrap = FlexWrap.wrap
    }
    "#hat_boarder_vertical_1" {
        left = 33.pct
    }
    "#hat_boarder_vertical_2" {
        right = 33.pct
    }
    "#hat_boarder_horizontal_1" {
        top = 33.pct
    }
    "#hat_boarder_horizontal_2" {
        bottom = 33.pct
    }
}

private fun BODY.gamepadBody() = div {
    id = "gamepad_container"
    div("parts button button_normal button_circle button_text") {
        id = "button_a"
        attributes["data-key"] = "button_a"
        p {
            +"A"
        }
    }
    div("parts button button_normal button_circle button_text") {
        id = "button_b"
        attributes["data-key"] = "button_b"
        p {
            +"B"
        }
    }
    div("parts button button_normal button_circle button_text") {
        id = "button_x"
        attributes["data-key"] = "button_x"
        p {
            +"X"
        }
    }
    div("parts button button_normal button_circle button_text") {
        id = "button_y"
        attributes["data-key"] = "button_y"
        p {
            +"Y"
        }
    }

    div("parts button button_lr button_text") {
        id = "button_l"
        attributes["data-key"] = "button_l"
        p {
            +"L"
        }
    }
    div("parts button button_lr button_text") {
        id = "button_zl"
        attributes["data-key"] = "button_zl"
        p {
            +"ZL"
        }
    }
    div("parts button button_lr button_text") {
        id = "button_r"
        attributes["data-key"] = "button_r"
        p {
            +"R"
        }
    }
    div("parts button button_lr button_text") {
        id = "button_zr"
        attributes["data-key"] = "button_zr"
        p {
            +"ZR"
        }
    }

    div("parts button button_mini button_circle button_text") {
        id = "button_minus"
        attributes["data-key"] = "button_select"
        p {
            +"-"
        }
    }
    div("parts button button_mini button_circle button_text") {
        id = "button_plus"
        attributes["data-key"] = "button_start"
        p {
            +"+"
        }
    }
    div("parts button button_mini") {
        id = "button_capture"
        attributes["data-key"] = "button_capture"
    }
    div("parts button button_mini button_circle") {
        id = "button_home"
        attributes["data-key"] = "button_home"
    }

    div("parts button button_mini button_text") {
        id = "button_l_stick"
        attributes["data-key"] = "button_l_stick"
        p {
            +"L"
        }
    }
    div("parts button_stick_reset button_text") {
        id = "button_stick_left_reset"
        p {
            +"+"
        }
    }
    div("parts stick") {
        id = "stick_left"
        attributes["data-key"] = "stick_left"
        div("reticle")
        div("stick_actual")
        div("stick_cursor")
    }
    div("parts button button_mini button_text") {
        id = "button_r_stick"
        attributes["data-key"] = "button_r_stick"
        p {
            +"R"
        }
    }
    div("parts button_stick_reset button_text") {
        id = "button_stick_right_reset"
        p {
            +"+"
        }
    }
    div("parts stick") {
        id = "stick_right"
        attributes["data-key"] = "stick_right"
        div("reticle")
        div("stick_actual")
        div("stick_cursor")
    }
    div("parts hat") {
        id = "hat"
        div("hat_direction hat_button button_text") {
            attributes["data-key"] = "7"
            p {
                +"↖"
            }
        }
        div("hat_direction hat_button button_text") {
            attributes["data-key"] = "0"
            p {
                +"↑"
            }
        }
        div("hat_direction hat_button button_text") {
            attributes["data-key"] = "1"
            p {
                +"↗"
            }
        }
        div("hat_direction hat_button button_text") {
            attributes["data-key"] = "6"
            p {
                +"←"
            }
        }
        div("hat_direction hat_button hat_button_selected") {
            attributes["data-key"] = "8"
        }
        div("hat_direction hat_button button_text") {
            attributes["data-key"] = "2"
            p {
                +"→"
            }
        }
        div("hat_direction hat_button button_text") {
            attributes["data-key"] = "5"
            p {
                +"↙"
            }
        }
        div("hat_direction hat_button button_text") {
            attributes["data-key"] = "4"
            p {
                +"↓"
            }
        }
        div("hat_direction hat_button button_text") {
            attributes["data-key"] = "3"
            p {
                +"↘"
            }
        }
        div("hat_boarder hat_boarder_vertical") {
            id = "hat_boarder_vertical_1"
        }
        div("hat_boarder hat_boarder_vertical") {
            id = "hat_boarder_vertical_2"
        }
        div("hat_boarder hat_boarder_horizontal") {
            id = "hat_boarder_horizontal_1"
        }
        div("hat_boarder hat_boarder_horizontal") {
            id = "hat_boarder_horizontal_2"
        }
    }
}
