
buttons = { }

buttonExists = (name) ->
    button = buttons[name]
    button and button.length # b/c jQuery

press = (button) ->
    unless buttonExists button
        buttons[button] = $ buttonElements[button]
    buttons[button].click()

connection = new WebSocket('ws://localhost:8886/controls')

connection.onmessage = ({data}) -> press data

setTimeout ->
    injectScript('http://code.jquery.com/jquery-2.1.0.min.js') unless $?
, 500

