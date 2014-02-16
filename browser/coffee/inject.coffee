
injectScript = (url) ->
    script = document.createElement 'script'
    script.src = url
    (document.head or document.documentElement).appendChild script
    