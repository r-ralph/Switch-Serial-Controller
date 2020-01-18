package ms.ralph.switch.serial.controller.view

import kotlinx.css.Align
import kotlinx.css.CSSBuilder
import kotlinx.css.Color
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.FlexWrap
import kotlinx.css.FontWeight
import kotlinx.css.JustifyContent
import kotlinx.css.em
import kotlinx.css.flex
import kotlinx.css.margin
import kotlinx.css.padding
import kotlinx.css.px
import kotlinx.html.DIV
import kotlinx.html.div
import kotlinx.html.p

fun CSSBuilder.includePanelViewCss() {
    rule(".flex_panel") {
        flex(1.0, 1.0, 0.px)
        display = Display.flex
        flexDirection = FlexDirection.column
        flexWrap = FlexWrap.nowrap
    }
    rule(".flex_panel_header") {
        display = Display.flex
        padding(0.px, 16.px)
        alignItems = Align.center
        backgroundColor = Color.lightGray
        justifyContent = JustifyContent.spaceBetween
    }
    rule(".flex_panel_header_label") {
        margin(0.5.em, 0.px)
        fontSize = 1.17.em
        fontWeight = FontWeight.bold
    }
    rule(".flex_panel_body") {
        flex(1.0, 1.0, 0.px)
    }
}

fun DIV.flexPanel(
    configuration: DIV.() -> Unit
) = div("flex_panel") {
    configuration()
}

fun DIV.flexPanelHeader(
    title: String,
    configuration: DIV.() -> Unit = {}
) = div("flex_panel_header") {
    p("flex_panel_header_label") {
        +title
    }
    configuration()
}

fun DIV.flexPanelBody(
    classes: String? = null,
    configuration: DIV.() -> Unit
) = div(listOfNotNull("flex_panel_body", classes).joinToString(separator = " ")) {
    configuration()
}
