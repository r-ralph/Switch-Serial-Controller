package ms.ralph.switch.serial.controller.view.util.mdc

import kotlinx.css.Align
import kotlinx.css.CSSBuilder
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.Position
import kotlinx.css.flex
import kotlinx.css.px
import kotlinx.html.FlowContent
import kotlinx.html.FlowOrInteractiveOrPhrasingContent
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.id
import kotlinx.html.span

fun CSSBuilder.includeMdcButtonCss() {
    ".custom-mdc-switch_left_label" {
        display = Display.flex
        flexDirection = FlexDirection.row
        alignItems = Align.baseline
    }
    ".custom-mdc-switch_left_label > label" {
        flex(1.0, 1.0, 0.px)
    }
    ".custom-mdc-switch_left_label_control" {
        position = Position.relative
    }
}

fun FlowOrInteractiveOrPhrasingContent.mdcUnelevatedButton(
    id: String,
    label: String
) {
    val classes = mutableListOf("mdc-button", "mdc-button--unelevated")
    button(classes = classes.joinToString(separator = " ")) {
        this.id = id
        div("mdc-button__ripple")
        span("mdc-button__label") {
            +label
        }
    }
}
