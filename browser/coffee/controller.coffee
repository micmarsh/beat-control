
buttons = { }

press = (button) ->
    unless buttons[button]
        buttons[button] = $ buttonElements[button]
    buttons[button].click()

connection = new WebSocket('ws://localhost:6666/controls')

connection.onmessage = ({data}) ->
    press data

injectScript('http://code.jquery.com/jquery-2.1.0.min.js') unless $?
