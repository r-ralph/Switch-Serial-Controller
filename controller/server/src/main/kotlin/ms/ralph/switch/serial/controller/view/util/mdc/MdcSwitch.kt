package ms.ralph.switch.serial.controller.view.util.mdc

import kotlinx.css.Align
import kotlinx.css.CSSBuilder
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.Position
import kotlinx.css.flex
import kotlinx.css.px
import kotlinx.html.FlowContent
import kotlinx.html.checkBoxInput
import kotlinx.html.div
import kotlinx.html.id
import kotlinx.html.label
import kotlinx.html.role

fun CSSBuilder.includeMdcSwitchLeftLabelCss() {
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

fun FlowContent.mdcSwitchLeftLabel(
    id: String,
    widthClass: String,
    label: String,
    isSelected: Boolean
) {
    val classes = mutableListOf("mdc-switch")
    if (widthClass.isNotBlank()) {
        classes.add(widthClass)
    }
    if (isSelected) {
        classes.add("mdc-switch--checked")
    }
    div(classes.joinToString(separator = " ")) {
        this.id = id
        div("custom-mdc-switch_left_label") {
            label {
                htmlFor = "${id}__for_label"
                +label
            }
            div("custom-mdc-switch_left_label_control") {
                div("mdc-switch__track")
                div("mdc-switch__thumb-underlay") {
                    div("mdc-switch__thumb") {
                        checkBoxInput(classes = "mdc-switch__native-control") {
                            this.id = "${id}__for_label"
                            role = "switch"
                            if(isSelected){
                                attributes["checked"] = ""
                            }
                        }
                    }
                }
            }
        }
    }
}
