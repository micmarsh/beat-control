LOCATION = window.location.hostname

groovesharkElements =
    play: 'play-pause'
    forward: 'play-next'
    back: 'play-prev'
rdioElements =
    play:  'play_pause'
    forward: 'next'
    back: 'prev'
youtubeElements =
    play: do ->
        playClass = 'html5-play-button'
        pauseClass = 'html5-pause-button'
        check = getElement playClass
        if check then playClass else pauseClass
    forward: 'watch7-playlist-bar-next-button'
    back: 'watch7-playlist-bar-prev-button'

elements =
    'grooveshark.com': groovesharkElements
    'www.grooveshark.com': groovesharkElements
    'rdio.com': rdioElements
    'www.rdio.com': rdioElements
    'youtube.com': youtubeElements
    'www.youtube.com': youtubeElements

validLocations = Object.keys elements

buttonElements = elements[LOCATION]