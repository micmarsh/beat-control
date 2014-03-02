module Main where
import Graphics.Input (button)

initialState = {
        play = "control 1",
        forward = "control 8",
        back = "control 7"
    }

keys = ["play", "forward", "back"]

makeButton : String -> (Element, Signal String)
makeButton label =
    let (element, signal) = button "change"
        goodSignal = lift (\n -> label) signal
    in (element, goodSignal)

buttonPairs = map makeButton keys
buttonElements = map fst buttonPairs
buttonSignals = map snd buttonPairs

buttonInfo {play, forward, back} = zip [ 
        ("play", play),
        ("forward", forward),
        ("back", back)
    ] buttonElements

rowView ((label, setting), button) = 
    flow right [
        asText label, 
        asText setting,
        button
    ]

main = flow down <| map rowView <| buttonInfo initialState
