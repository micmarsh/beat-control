
PREFIX = 'ws://localhost:8886/'

changes = new WebSocket PREFIX + 'changes'

# incoming = new WebSocket PREFIX + 'config'
# incoming.onmessage = (config) ->
#     console.log config