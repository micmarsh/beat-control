#!/bin/sh
mkdir javascripts

cat coffee/inject.coffee coffee/pageinfo.coffee coffee/controller.coffee > _controller.coffee
coffee -cj javascripts/controller.js _controller.coffee

cat coffee/inject.coffee coffee/pageinfo.coffee coffee/injectscripts.coffee > _injectscripts.coffee
coffee -cj javascripts/injectscripts.js _injectscripts.coffee



cat coffee/mainpage.coffee coffee/keypresses.coffee > _mainpage.coffee
coffee -cj javascripts/main.js _mainpage.coffee

elm --make -o elm/Main.elm 
mv build/elm/Main.js javascripts/elm-main.js


rm -rf build/
rm -rf cache/
rm _* 