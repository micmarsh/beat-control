
{div, h1, p, span, button} = React.DOM

changing = null

Hotkeys = React.createClass 

    displayName: 'Hotkeys'

    setText: (which, text) ->
        {settings} = @state
        settings[which] = text
        @setState {settings}

    appendText: (which, text) ->
        oldText = @state.settings[which]
        unless which in oldText
            @setText which, oldText + text

    getInitialState: ->
        settings:
            play: 'control 1'
            back: 'control 7'
            forward: 'control 8'

    click: (which) -> =>
        console.log which
        # elm.ports.changing.send which
        changing = which
        @setText which, "Change Me!"

    render: ->
        div null, 
            h1 null, "Change Ur Hotkeys"
            for name, setting of @state.settings
                p null, "#{name}: ", span(null, setting), button {onClick: @click name}, 'change'

hotkeys = Hotkeys()

React.renderComponent hotkeys, document.getElementById 'mainDisplay'




# elm = Elm.worker Elm.Main,
#     state: hotkeys.state.settings
#     changing: 'none'
#     characters: 'none'
#     modifiers: 'none'

# elm.ports.newState.subscribe (state) ->
#     hotkeys.setState
#         settings: state