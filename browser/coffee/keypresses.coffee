
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

document.addEventListener 'keydown', ({keycode}) ->

    if isModifier keycode
        elm.ports.modifiers.send modifierCodes[keycode]
    else 
        special = specialChars[keycode]

        character = special or
            String.fromCharCode(keycode).toLowerCase()

        elm.ports.characters.send character
