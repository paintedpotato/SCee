# SCee v4
So this app can detect faces (>0) and polls every few seconds before alerting you how many faces it has detected.
The bug to do with the switchingCamera has been eliminated after resetting random2 = 0; among the first executions within the switchCamera() method. Slight oversight.

Other ideas:
* to randomize the toasts that will be displayed if it detects 1 or more faces.. but my job hasn't changed i.e
* to create toasts which will later be full on conversational texts displayed somewhere perhaps on the side, perhaps via IBM api or we'll think about what method is best in user interactivity, to communicate to the user via their displayed facial emotions.
