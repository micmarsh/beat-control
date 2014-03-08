
changing = null

PLACEHOLDER = "Change Me!"

shouldAppend = (oldText, text) ->
    return true if text.length is 1
    for code, char of specialChars
        if char is text
            return true
    oldText.indexOf(text) is -1

Hotkeys = React.createClass 

    displayName: 'Hotkeys'

    setKeys: do ->
        keyOrder = ['play', 'back', 'forward']
        (keys) ->
            settings = { }
            for key in keyOrder
               settings[key] = keys[key]
            @setState {settings}

    setText: (which, text) ->
        {settings} = @state
        settings[which] = text
        @setKeys settings

    appendText: (which, text) ->
        oldText = @state.settings[which]

        if oldText is PLACEHOLDER
            newText = text
        else if shouldAppend oldText, text
            newText = oldText + text
        else
            newText = oldText

        @setText which, newText

        return newText

    getInitialState: ->
        settings:
            play: 'control 1'
            back: 'control 7'
            forward: 'control 8'

    click: (which) -> =>
        console.log which
        changing = which
        @setText which, PLACEHOLDER

    render: do ->
        {div, h1, p, span, button} = React.DOM
        ->            
            div null, 
                h1 null, "Change Ur Hotkeys"
                for name, setting of @state.settings
                    p null, "#{name}: ", span(null, setting), button {onClick: @click name}, 'change'

hotkeys = Hotkeys()

React.renderComponent hotkeys, document.getElementById 'mainDisplay'

