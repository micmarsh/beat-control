
PREFIX = 'ws://localhost:8886/'
changes = null

reopen = (opener, delay) -> ->
    setTimeout ->
        opener delay * 1.5
    , 100 

do openControls = (delay = 1000)->
    changes = new WebSocket PREFIX + 'controls'
    changes.onmessage = (message) ->
        {data} = message
        settings = JSON.parse data
        hotkeys.setKeys settings
    changes.onclose = reopen openControls, delay

do openErrors = (delay = 1000) ->
    errors = new WebSocket PREFIX + 'errors'
    errors.onmessage = (message) ->
        {data} = message
        console.log "woah an error!"
        console.log data
    errors.onclose = reopen openErrors, delay
