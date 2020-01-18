window.addEventListener("DOMContentLoaded", function() {
    initMaterialComponents();
    window.addEventListener("message", function (event) {
        if (typeof event.data !== "string") {
            return;
        }
        var commands = event.data.split(":")
        if (commands.length == 3 && commands[0] == "gamepad") {
            updateCurrentStatus(commands[1], commands[2]);
            updateInputTextToCurrentStatus();
            if (currentConfig.send_immediately) {
                sendCurrentButtonsViaWebSocket();
            }
        } else if (commands.length == 2 && commands[0] == "gamepad") {
            if (commands[1] == "reset") {
                resetAllStatus();
            }
            if (currentConfig.send_immediately) {
                sendCurrentButtonsViaWebSocket();
            }
        }
    });
    updateInputTextToCurrentStatus();
});

var mdcControls = {};
var ws = null;

function initMaterialComponents() {
    {
        var element = document.getElementById("button_send");
        var mdcControl = new mdc.ripple.MDCRipple(element);
        element.addEventListener("click", function (event) {
            sendCurrentButtons();
        });
        mdcControls["panel_gamepad_send"] = mdcControl;
    }
    {
        var element = document.getElementById("button_start_macro");
        var mdcControl = new mdc.ripple.MDCRipple(element);
        element.addEventListener("click", function (event) {
            sendMacro();
        });
        mdcControls["panel_macro_start"] = mdcControl;
    }
    {
        var element = document.getElementById("panel_control_select_port");
        var mdcControl = new mdc.select.MDCSelect(element);
        if (element.children[1].children[0].childElementCount > 0) {
            mdcControl.selectedIndex = 0;
        }
        element.addEventListener("change", function (event) {
            console.log(event);
        });
        mdcControls["panel_control_select_port"] = mdcControl;
    }
    {
        var element = document.getElementById("panel_control_switch_direct_send");
        element.getElementsByTagName("input")[0].onchange = function(e) {
            currentConfig.send_immediately = e.target.checked;
            if (ws != null) {
                ws.close();
                ws = null;
            }
            if (currentConfig.send_immediately) {
                var tempWs = new WebSocket("ws://" + document.location.host + "/api/command/send/ws/");
                tempWs.addEventListener("open", e => {
                    console.log("ws opened")
                    ws = tempWs;
                });
                tempWs.addEventListener("close", e => {
                    console.log("ws closed");
                });
                tempWs.addEventListener("error", e => {
                    console.log("ws error: " + e);
                });
            }
        };
        var mdcControl = new mdc.switchControl.MDCSwitch(element);
        mdcControls["panel_control_switch_direct_send"] = mdcControl;
    }
    {
        var element = document.getElementById("panel_control_switch_hold_gamepad");
        element.getElementsByTagName("input")[0].onchange = function(e) {
            document.getElementById("gamepad_container_frame").contentWindow.postMessage(
                "control:hold_gamepad:" + e.target.checked,
                "*"
            );
        };
        var mdcControl = new mdc.switchControl.MDCSwitch(element);
        mdcControls["panel_control_switch_hold_gamepad"] = mdcControl;
    }
}

var currentConfig = {
    send_immediately: false
}
var currentStatus = {
    a : false,
    b : false,
    x : false,
    y : false,
    l : false,
    r : false,
    zl : false,
    zr : false,
    l_stick : false,
    r_stick : false,
    start : false,
    select : false,
    home : false,
    capture : false,
    hat : 8,
    left_stick_degrees : 0,
    left_stick_tilt : 0,
    right_stick_degrees : 0,
    right_stick_tilt : 0
}

function updateCurrentStatus(target, value) {
    switch (target) {
        case "button_a":
            currentStatus.a = parseBoolean(value);
            break;
        case "button_b":
            currentStatus.b = parseBoolean(value);
            break;
        case "button_x":
            currentStatus.x = parseBoolean(value);
            break;
        case "button_y":
            currentStatus.y = parseBoolean(value);
            break;
        case "button_l":
            currentStatus.l = parseBoolean(value);
            break;
        case "button_r":
            currentStatus.r = parseBoolean(value);
            break;
        case "button_zl":
            currentStatus.zl = parseBoolean(value);
            break;
        case "button_zr":
            currentStatus.zr = parseBoolean(value);
            break;
        case "button_l_stick":
            currentStatus.l_stick = parseBoolean(value);
            break;
        case "button_r_stick":
            currentStatus.r_stick = parseBoolean(value);
            break;
        case "button_start":
            currentStatus.start = parseBoolean(value);
            break;
        case "button_select":
            currentStatus.select = parseBoolean(value);
            break;
        case "button_home":
            currentStatus.home = parseBoolean(value);
            break;
        case "button_capture":
            currentStatus.capture = parseBoolean(value);
            break;
        case "hat":
            currentStatus.hat = parseInt(value);
            break;
        case "stick_left":
            var args = value.split(",");
            currentStatus.left_stick_degrees = parseFloat(args[0]);
            currentStatus.left_stick_tilt = parseFloat(args[1]);
            break;
        case "stick_right":
            var args = value.split(",");
            currentStatus.right_stick_degrees = parseFloat(args[0]);
            currentStatus.right_stick_tilt = parseFloat(args[1]);
            break;
    }
}

function parseBoolean(string) {
    return string.toLowerCase() === "true";
}

function updateInputTextToCurrentStatus() {
    document.getElementById("gamepad_raw_value_input").value = getCurrentMacroValue();
}

function resetAllStatus() {
    currentStatus.a = false;
    currentStatus.b = false;
    currentStatus.x = false;
    currentStatus.y = false;
    currentStatus.l = false;
    currentStatus.r = false;
    currentStatus.zl = false;
    currentStatus.zr = false;
    currentStatus.l_stick = false;
    currentStatus.r_stick = false;
    currentStatus.start = false;
    currentStatus.select = false;
    currentStatus.home = false;
    currentStatus.capture = false;
    currentStatus.hat = 8;
    currentStatus.left_stick_degrees = 0;
    currentStatus.left_stick_tilt = 0;
    currentStatus.right_stick_degrees = 0;
    currentStatus.right_stick_tilt = 0;
    updateInputTextToCurrentStatus()
}

function getCurrentMacroValue() {
    var v = "";
    if (currentStatus.a) {
        v += "A;";
    }
    if (currentStatus.b) {
        v += "B;";
    }
    if (currentStatus.x) {
        v += "X;";
    }
    if (currentStatus.y) {
        v += "Y;";
    }
    if (currentStatus.l) {
        v += "L;";
    }
    if (currentStatus.r) {
        v += "R;";
    }
    if (currentStatus.zl) {
        v += "ZL;";
    }
    if (currentStatus.zr) {
        v += "ZR;";
    }
    if (currentStatus.l_stick) {
        v += "LCLICK;";
    }
    if (currentStatus.r_stick) {
        v += "RCLICK;";
    }
    if (currentStatus.start) {
        v += "START;";
    }
    if (currentStatus.select) {
        v += "SELECT;";
    }
    if (currentStatus.home) {
        v += "HOME;";
    }
    if (currentStatus.capture) {
        v += "CAPTURE;";
    }
    if (currentStatus.hat != 8) {
        v += "HAT@" + currentStatus.hat + ";";
    }
    if (currentStatus.left_stick_tilt != 0) {
        v += "LSTICK@" + currentStatus.left_stick_degrees + "," + currentStatus.left_stick_tilt + ";";
    }
    if (currentStatus.right_stick_tilt != 0) {
        v += "RSTICK@" + currentStatus.right_stick_degrees + "," + currentStatus.right_stick_tilt + ";";
    }
    return v.slice(0, -1);
}

function sendCurrentButtons() {
    (async() => {
      try {
        var data = createSendData();
        const headers = {
            "Accept": "application/json",
            "Content-Type": "application/json"
        };
        const response = await fetch("api/command/send", { method: "POST", headers: headers, body: JSON.stringify(data) });
        console.log(response.status);
      } catch (e) {
        console.log("error!")
      }
    })();
}

function sendCurrentButtonsViaWebSocket() {
    var port = mdcControls["panel_control_select_port"].value;
    var command = document.getElementById("gamepad_raw_value_input").value;
    var payload = "send;" + port + ";" + command;
    var localWs = ws;
    if (localWs != null) {
        localWs.send(payload);
    }
}

function createSendData() {
    return {
        "port":  mdcControls["panel_control_select_port"].value,
        "value": document.getElementById("gamepad_raw_value_input").value
    }
}

function sendMacro() {
    (async() => {
      try {
        var data = createMacroData();
        const headers = {
            "Accept": "application/json",
            "Content-Type": "application/json"
        };
        const response = await fetch("api/command/macro", { method: "POST", headers: headers, body: JSON.stringify(data) });
        console.log(response.status);
      } catch (e) {
        console.log("error!")
      }
    })();
}

function createMacroData() {
    return {
        "port": mdcControls["panel_control_select_port"].value,
        "macro": document.getElementById("macro_text_input").value
    }
}
