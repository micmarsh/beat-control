# both of these defined in pageinfo
unless LOCATION in validLocations
    return

inject = do ->
    injected = false
    ->
        unless injected
            injectScript chrome.extension.getURL "javascripts/controller.js" +
            "?nocache=#{new Date().getTime()}"
            injected = true

window.onload = inject
setTimeout inject, 5000