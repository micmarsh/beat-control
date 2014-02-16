# LOCATION defined in pageinfo
correctLocation = LOCATION in validLocations

unless correctLocation
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