package ms.ralph.switch.serial.controller.view

import kotlinx.css.CSSBuilder
import kotlinx.css.Color
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.FlexWrap
import kotlinx.css.body
import kotlinx.css.html
import kotlinx.css.iframe
import kotlinx.css.margin
import kotlinx.css.p
import kotlinx.css.padding
import kotlinx.css.pct
import kotlinx.css.px

val Color.Companion.lightGray: Color
    get() = Color("#dddddd")

fun CSSBuilder.includeResetCss() {
    html {
        height = 100.pct
        width = 100.pct
        margin(0.px)
        padding(0.px)
    }
    body {
        height = 100.pct
        width = 100.pct
        margin(0.px)
        padding(0.px)
    }
    iframe {
        border = "0"
    }
    p {
        margin(0.px)
        padding(0.px)
    }
}

fun CSSBuilder.includeCommonCss() {
    rule(".vertical_flex_container") {
        display = Display.flex
        flexDirection = FlexDirection.column
        flexWrap = FlexWrap.nowrap
    }
}
