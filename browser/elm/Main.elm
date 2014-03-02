module Main where

--initialState = {
--        play = "control 1",
--        forward = "control 8",
--        back = "control 7"
--    }

--keys = ["play", "forward", "back"]

--makeButton : String -> (Element, Signal String)
--makeButton label =
--    let (element, signal) = button "change"
--        goodSignal = lift (\n -> label) signal
--    in (element, goodSignal)

--buttonPairs = map makeButton keys
--buttonElements = map fst buttonPairs
--buttonSignals = map snd buttonPairs

--buttonInfo {play, forward, back} = zip [ 
--        ("play", play),
--        ("forward", forward),
--        ("back", back)
--    ] buttonElements

--rowView ((label, setting), button) = 
--    flow right [
--        asText label, 
--        asText setting,
--        button
--    ]

--main = flow down <| map rowView <| buttonInfo initialState


lookup state string =
    case string of
        "play" -> Just (.play state)
        "back" -> Just (.back state)
        "forward" -> Just (.forward state)
        _ -> Nothing

set state string value =
    case string of 
        "play" -> {state | play <- value}
        "back" -> {state | back <- value}
        "forward" -> {state | forward <- value}
        _ -> state

port state : Signal {play:String, back:String, forward:String}
port changing : Signal String

port newState : Signal {play:String, back:String, forward:String}
port newState = lift2 (\s str -> set s str "change me!") state changing
