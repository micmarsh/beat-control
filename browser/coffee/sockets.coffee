
PREFIX = 'ws://localhost:8886/'
changes = null

do open = (delay = 1000)->
    changes = new WebSocket PREFIX + 'controls'
    changes.onmessage = (message) ->
        {data} = message
        settings = JSON.parse data
        hotkeys.setKeys settings
    changes.onclose = ->
        setTimeout ->
            open delay * 1.5
        , 100