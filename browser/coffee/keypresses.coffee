
modifierCodes =
    16: 'shift'
    17: 'control'
    18: 'alt'

specialChars = 
    32: "space"
    9: "tab"
    37: "left"
    38: "up"
    39: "right"
    40: "down"

isModifier = (code) -> Boolean modifierCodes[code]

document.addEventListener 'keydown', ({keyCode}) ->
    if Boolean changing
        if isModifier keyCode
            hotkeys.appendText changing, "#{modifierCodes[keyCode]} "
        else 
            special = specialChars[keyCode]
            character = special or String.fromCharCode(keyCode).toLowerCase()
            changeTo = hotkeys.appendText changing, "#{character}"
            message = {}

            message[changing] = changeTo
            changes.send JSON.stringify message

            changing = null
