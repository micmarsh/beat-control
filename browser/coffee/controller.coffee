
buttons = { }

buttonExists = (name) ->
    button = buttons[name]
    button and button.length # b/c jQuery

handlePlayPause = do ->
    playing = true #ideally, you'll want something more correct than this
    # may be hard to standardize that across all of these diff things, tho
    (fn) -> (button) ->
        if playing is true and button is 'play'
            button = 'pause'
            playing = false
        else
            playing = true
        fn button

press = handlePlayPause (button) ->
    unless buttonExists button
        buttons[button] = $ buttonElements[button]
    [singleButton] = buttons[button]
    singleButton.click() # it's probably helpful to throw exceptions here

do open = (delay = 1000) ->
    connection = new WebSocket('ws://localhost:8888/keypresses')
    connection.onopen = -> 
        delay = 1000
        console.log 'connected to controls'
    connection.onmessage = ({data}) -> press data
    connection.onclose = -> setTimeout ->
        newDelay = delay * 1.5
        console.log "server not available, trying again in #{newDelay} milliseconds"
        open newDelay
    , delay

setTimeout ->
    injectScript('https://code.jquery.com/jquery-2.1.0.min.js') unless $?
, 500

