
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
            # elm.ports.modifiers.send character
            hotkeys.appendText changing, "#{modifierCodes[keyCode]} "
        else 
            special = specialChars[keyCode]

            character = special or String.fromCharCode(keyCode).toLowerCase()

            changeTo = hotkeys.appendText changing, "#{character}"
           
            console.log changeTo

            message = {}
            message[changing] = changeTo
            changes.send JSON.stringify message

            changing = null
            # elm.ports.characters.send character
