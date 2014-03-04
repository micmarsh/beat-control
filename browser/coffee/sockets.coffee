
PREFIX = 'ws://localhost:8886/'
changes = null

do open = (delay = 1000)->
    changes = new WebSocket PREFIX + 'changes'8
    changes.onmessage = (message) ->
        {data} = message
        settings = JSON.parse data
        hotkeys.setState {settings}
    changes.onclose = ->
        setTimeout ->
            open delay * 1.5
        , 100