
{div, h1, p, span, button} = React.DOM

Hotkeys = React.createClass 

    displayName: 'Hotkeys'

    getInitialState: ->
        settings:
            play: 'control 1'
            back: 'control 7'
            forward: 'control 8'

    click: (which) -> ->
        console.log which
        elm.ports.changing.send which

    render: ->
        div null, 
            h1 null, "Change Ur Hotkeys"
            for name, setting of @state.settings
                p null, "#{name}: ", span(null, setting), button {onClick: @click name}, 'change'

hotkeys = Hotkeys()

React.renderComponent hotkeys, document.getElementById 'mainDisplay'

elm = Elm.worker Elm.Main,
    state: hotkeys.state.settings
    changing: 'none'
    characters: 'none'
    modifiers: 'none'

elm.ports.newState.subscribe (state) ->
    hotkeys.setState
        settings: state