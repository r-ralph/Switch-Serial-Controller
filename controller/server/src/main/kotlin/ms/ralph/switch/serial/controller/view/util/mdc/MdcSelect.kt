package ms.ralph.switch.serial.controller.view.util.mdc

import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.i
import kotlinx.html.id
import kotlinx.html.label
import kotlinx.html.li
import kotlinx.html.role
import kotlinx.html.ul

data class MdcSelectItem(val dataValue: String, val label: String)

fun FlowContent.mdcOutlinedSelect(
    id: String,
    widthClass: String,
    label: String,
    items: List<MdcSelectItem>
) {
    div("mdc-select mdc-select--outlined") {
        this.id = id
        div("mdc-select__anchor $widthClass".trim()) {
            i("mdc-select__dropdown-icon")
            div("mdc-select__selected-text") {
                attributes["tabindex"] = "-1"
                attributes["aria-disabled"] = "false"
            }
            div("mdc-notched-outline") {
                div("mdc-notched-outline__leading")
                div("mdc-notched-outline__notch") {
                    label("mdc-floating-label") {
                        +label
                    }
                }
                div("mdc-notched-outline__trailing")
            }
        }
        div("mdc-select__menu mdc-menu mdc-menu-surface $widthClass".trim()) {
            role = "listbox"
            ul("mdc-list") {
                items.forEach {
                    li("mdc-list-item") {
                        role = "option"
                        attributes["data-value"] = it.dataValue
                        +it.label
                    }
                }
            }
        }
    }
}
