
LOCATION = window.location.hostname

groovesharkElements =
    play: '#play-pause'
    pause:  '#play-pause'
    forward: '#play-next'
    back: '#play-prev'
rdioElements =
    play:  '.play_pause'
    pause:  '.play_pause'
    forward: '.next'
    back: '.prev'
theListElements = 
    play: '.fa-play'
    pause: '.fa-pause'
    forward: '.fa-step-forward'
    back: '.fa-step-backward'

# youtubeElements =
#     play: do ->
#         playClass = 'html5-play-button'
#         pauseClass = 'html5-pause-button'
#         check = getElement playClass
#         if check then playClass else pauseClass
#     forward: 'watch7-playlist-bar-next-button'
#     back: 'watch7-playlist-bar-prev-button'

elements =
    'grooveshark.com': groovesharkElements
    'www.grooveshark.com': groovesharkElements
    'rdio.com': rdioElements
    'www.rdio.com': rdioElements
    'brandly.github.io': theListElements
    # 'youtube.com': youtubeElements
    # 'www.youtube.com': youtubeElements

validLocations = Object.keys elements

buttonElements = elements[LOCATION]