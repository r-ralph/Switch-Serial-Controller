window.addEventListener("DOMContentLoaded", function() {
    Array.prototype.forEach.call(document.getElementsByClassName("stick_cursor"), function(element) {
        element.addEventListener("mousedown", handleMouseDown, false);
    });
    Array.prototype.forEach.call(document.getElementsByClassName("button"), function(element) {
        element.addEventListener("click", handleButtonClick, false);
    });
    Array.prototype.forEach.call(document.getElementsByClassName("button"), function(element) {
        element.addEventListener("mousedown", handleButtonMouseDown, false);
    });
    Array.prototype.forEach.call(document.getElementsByClassName("hat_button"), function(element) {
        element.addEventListener("click", handleHatButtonClick, false);
    });
    Array.prototype.forEach.call(document.getElementsByClassName("hat_button"), function(element) {
        element.addEventListener("mousedown", handleHatButtonMouseDown, false);
    });
    window.addEventListener("message", function (event) {
        if (typeof event.data !== "string") {
            return;
        }
        var commands = event.data.split(":")
        if (commands.length == 3 && commands[0] === "control") {
            updateControlStatus(commands[1], commands[2]);
        }
    });
    document.getElementById("button_stick_left_reset").onclick = function() {
        resetGamepad(document.getElementById("stick_left"));
        return false;
    };
    document.getElementById("button_stick_right_reset").onclick = function() {
        resetGamepad(document.getElementById("stick_right"));
        return false;
    };
});

var currentControlSetting = {
    hold_gamepad: true
};

function updateControlStatus(target, value) {
    switch (target) {
        case "hold_gamepad":
            currentControlSetting.hold_gamepad = parseBoolean(value);
            if (!currentControlSetting.hold_gamepad) {
                resetGamepad();
            }
            break;
    }
}

function resetGamepad() {
    var elements = Array.from(document.getElementsByClassName("button_selected"));
    elements.forEach(element => {
        element.classList.remove("button_selected");
    });
    elements = Array.from(document.getElementsByClassName("button_clicking"));
        elements.forEach(element => {
            element.classList.remove("button_clicking");
        });
    Array.prototype.forEach.call(document.getElementsByClassName("stick"), function(element) {
        resetStick(element);
    });
    setSelectedHat(8, false);
    window.parent.postMessage("gamepad:reset", "*");
}

function handleButtonClick(event) {
    if (!currentControlSetting.hold_gamepad) {
        return;
    }
    toggleButtonSelected(event.target);
}

function handleHatButtonClick(event) {
    if (!currentControlSetting.hold_gamepad) {
        return;
    }
    var hat = parseInt(event.target.dataset.key);
    setSelectedHat(hat, true);
}

function setSelectedHat(hat, notify) {
    Array.prototype.forEach.call(document.getElementsByClassName("hat_button"), function(element) {
        var newState = hat === parseInt(element.dataset.key);
        if (newState) {
            element.classList.add("hat_button_selected");
        } else {
            element.classList.remove("hat_button_selected");
        }
    });
    if (notify) {
        window.parent.postMessage("gamepad:hat:" + hat, "*");
    }
}

function handleHatButtonMouseDown(event) {
    if (currentControlSetting.hold_gamepad) {
        return;
    }
    var hat = parseInt(this.dataset.key)
    document.body.addEventListener("mouseup", handleHatButtonMouseUp, false);
    document.body.addEventListener("mouseleave", handleHatButtonMouseUp, false);
    setSelectedHat(hat, true)
}

function handleHatButtonMouseUp(event) {
    document.body.removeEventListener("mouseup", handleHatButtonMouseUp, false);
    document.body.removeEventListener("mouseleave", handleHatButtonMouseUp, false);
    setSelectedHat(8, true)
}

function handleButtonMouseDown(event) {
    if (currentControlSetting.hold_gamepad) {
        return;
    }
    this.classList.add("button_clicking");
    document.body.addEventListener("mouseup", handleButtonMouseUp, false);
    document.body.addEventListener("mouseleave", handleButtonMouseUp, false);
    window.parent.postMessage("gamepad:" + this.dataset.key + ":true", "*");
}

function handleButtonMouseUp(event) {
    var clickingElement = document.getElementsByClassName("button_clicking")[0];
    if (!clickingElement) {
        return;
    }
    clickingElement.classList.remove("button_clicking");
    document.body.removeEventListener("mouseup", handleButtonMouseUp, false);
    document.body.removeEventListener("mouseleave", handleButtonMouseUp, false);
    window.parent.postMessage("gamepad:" + clickingElement.dataset.key + ":false", "*");
}

function resetStick(stickElement) {
    var stickActual = stickElement.getElementsByClassName("stick_actual")[0];
    var stickCursor = stickElement.getElementsByClassName("stick_cursor")[0];
    stickActual.style.top = ((stickElement.clientHeight - stickActual.clientHeight) / 2) + "px";
    stickActual.style.left = ((stickElement.clientWidth - stickActual.clientWidth) / 2) + "px";
    stickCursor.style.top = ((stickElement.clientHeight - stickCursor.clientHeight) / 2) + "px";
    stickCursor.style.left = ((stickElement.clientWidth - stickCursor.clientWidth) / 2) + "px";
}

var x;
var y;

function handleMouseDown(event) {
    this.classList.add("stick_cursor_dragging");
    x = event.pageX - this.offsetLeft;
    y = event.pageY - this.offsetTop;
    document.body.addEventListener("mousemove", handleMouseMove, false);
    document.body.addEventListener("mouseup", handleMouseUp, false);
    document.body.addEventListener("mouseleave", handleMouseUp, false);
    this.addEventListener("mouseup", handleMouseUp, false);
}

function handleMouseMove(event) {
    event.preventDefault();
    var drag = document.getElementsByClassName("stick_cursor_dragging")[0];
    var minX = drag.clientWidth / -2;
    var minY = drag.clientHeight / -2;
    var maxX = drag.parentElement.clientWidth - drag.clientWidth / 2;
    var maxY = drag.parentElement.clientHeight - drag.clientHeight / 2;
    var newX = clamp(event.pageX - x, minX, maxX);
    var newY = clamp(event.pageY - y, minY, maxY);
    drag.style.top = newY + "px";
    drag.style.left = newX + "px";
    var xPercent = (newX - minX) / (maxX - minX);
    var yPercent = (newY - minY) / (maxY - minY);
    var key = drag.parentElement.dataset.key;

    var xVector = (xPercent - 0.5) * 2;
    var yVector = (0.5 - yPercent) * 2;
    var originalLength = Math.sqrt(xVector * xVector + yVector * yVector);
    var length = Math.min(originalLength, 1);
    var rad = Math.atan2(yVector, xVector)

    var actual = drag.parentElement.getElementsByClassName("stick_actual")[0];
    if (originalLength != length) {
        actual.style.width = "4px";
        actual.style.height = "4px";
    } else {
        actual.style.width = "2px";
        actual.style.height = "2px";
    }
    var xA = ((Math.cos(rad) * length) / 2 + 0.5) * drag.parentElement.clientWidth - actual.clientWidth / 2;
    var yA = (-(Math.sin(rad) * length) / 2 + 0.5) * drag.parentElement.clientHeight - actual.clientHeight / 2;
    actual.style.top = yA + "px";
    actual.style.left = xA + "px";
    var degree = rad * 360 / (2 * Math.PI);
    if (degree < 0) {
        degree += 360;
    }
    window.parent.postMessage("gamepad:" + key + ":" + degree + "," + length, "*");
}

function handleMouseUp(e) {
    var drag = document.getElementsByClassName("stick_cursor_dragging")[0];
    if (!drag) {
        return;
    }
    document.body.removeEventListener("mouseup", handleMouseUp, false);
    document.body.removeEventListener("mouseleave", handleMouseUp, false);
    document.body.removeEventListener("mousemove", handleMouseMove, false);
    drag.removeEventListener("mouseup", handleMouseUp, false);
    drag.classList.remove("stick_cursor_dragging");
    if (!currentControlSetting.hold_gamepad) {
        resetGamepad(drag.parentElement);
    }
}

function toggleButtonSelected(element) {
    var newState = !element.classList.contains("button_selected");
    if (newState) {
        element.classList.add("button_selected");
    } else {
        element.classList.remove("button_selected");
    }
    var key = element.dataset.key;
    window.parent.postMessage("gamepad:" + key + ":" + newState, "*");
}

function clamp(v, from, to) {
  if (v < from)
    return from;
  else if (v > to)
    return to;
  return v;
}

function parseBoolean(string) {
    return string.toLowerCase() === "true";
}
