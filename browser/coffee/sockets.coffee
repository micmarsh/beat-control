
PREFIX = 'ws://localhost:8886/'

changes = new WebSocket PREFIX + 'changes'
changes.onmessage = (message) ->
    {data} = message
    settings = JSON.parse data
    hotkeys.setState {settings}
# incoming = new WebSocket PREFIX + 'config'
# incoming.onmessage = (config) ->
#     console.log config