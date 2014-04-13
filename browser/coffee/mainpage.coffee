
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

    setError: (which, error) ->
        {errors} = @state
        errors[which] = "Error: Can't set key combination: #{error}"
        console.log @state.last
        @setState {errors, settings: @state.last}
        setTimeout =>
            errors[which] = ''
            @setState {errors}
        , 3500


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
        last: { }
        settings:
            play: 'control 1'
            back: 'control 7'
            forward: 'control 8'
        errors:
            play: ''
            back: ''
            forward: ''

    saveLast: ->
        clone = { }
        for key, value of @state.settings
            clone[key] = value
        @setState {last: clone}

    click: (which) -> =>
        console.log which
        changing = which
        @saveLast()
        @setText which, PLACEHOLDER

    render: do ->
        {div, h1, p, span, button} = React.DOM
        ->            
            div {id: 'main'}, 
                h1 null, "Hotkeys Settings"
                for name, setting of @state.settings
                    [       
                        p null, "#{name}: ", span(null, setting), button {onClick: @click name}, 'change'
                        if @state.errors[name] then p({className: 'error'}, @state.errors[name]) else ''
                    ]

LinkButton = React.createClass
    displayName: 'LinkButton'

    click: ->
        chrome.tabs.create {url: "http://localhost:8888"}

    render: do ->
        {button, div} = React.DOM
        ->
            div {id: 'main'},
                button {onClick: @click}, "Adjust Settings"

if window.chrome and chrome.runtime and chrome.runtime.id
    do ->
        button = LinkButton()
        React.renderComponent button, document.getElementById 'mainDisplay'
else
    hotkeys = Hotkeys()
    React.renderComponent hotkeys, document.getElementById 'mainDisplay'

