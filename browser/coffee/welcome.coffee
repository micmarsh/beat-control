
compatibleSites = 
    'Grooveshark': 'grooveshark.com'
    'Rdio': 'www.rdio.com'
    'Pandora': 'www.pandora.com'
    'The List': 'brandly.github.io/thelist'
    'Reddit Playlister': 'redditplayer.phoenixforgotten.com'

listItems = for name, url in compatibleSites
    "<li><a href='http://#{url}'>#{name}</a></li>"

list = "<ul>#{listItems.join('')}</ul>"

document.getElementById('sites').innerHTML = list