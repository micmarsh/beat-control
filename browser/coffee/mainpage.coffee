
{div, h1, p, span, button} = React.DOM

Hotkeys = React.createClass 

    displayName: 'Hotkeys'

    getInitialState: ->
        settings:
            play: 'control 1'
            forward: 'control 8'
            back: 'control 7'

    render: ->
        div null, 
            h1 null, "Change Ur Hotkeys"
            for name, setting of @state.settings
                p null, "#{name}: ", span(null, setting), button(null, 'change')

hotkeys = Hotkeys()

React.renderComponent hotkeys, document.getElementById 'mainDisplay'

setTimeout ->
    {state} = hotkeys
    state.settings['ffoooo'] = 'bar'
    hotkeys.setState state
,3000