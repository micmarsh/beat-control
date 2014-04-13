
PREFIX = 'ws://localhost:8888/'
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
    errors.onopen = -> console.log "yo"
    errors.onmessage = (message) ->
        {data} = message
        object = JSON.parse data
        for which, error of object
            hotkeys.setError which, error
        console.log "woah an error!"
        console.log data
    errors.onclose = reopen openErrors, delay
