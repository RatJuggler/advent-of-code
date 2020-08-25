# advent-of-code 2016

##### Day 1: No Time for a Taxicab
First part is straightforward as we are just updating the current location. For the second part I didn't want to mess around with
trying to find the intersection points of all the direction vectors so I built a history of every point visited and used a stream 
with lambdas to filter and reduce it.

##### Day 2: Bathroom Security
I started off by coding all the state changes explicitly and then refactored this into a more generic solution for defining keypads. 

##### Day 3: Squares With Three Sides
Simple to do in a long-handed way but couldn't think of a more elegant/generic approach for Part 2.

##### Day 4: Security Through Obscurity
Problem lends itself to using streams for much of the processing.

##### Day 5: How About a Nice Game of Chess?
The core solution code was straightforward but required a bit of rework to get a single method showing the password being filled in. 
Java doesn't have a nice "Curses" type library, so I didn't animate the output in the end. The bytes-to-hex-string method was pulled 
from StackOverflow just because.

##### Day 6: Signals and Noise
Started by building a imperative, statement oriented type solution and then reworked it to use a more declarative, expression 
oriented approach.

##### Day 7: Internet Protocol Version 7
Built a validator class to parse the IP and preform the required checks. Simple to then stream the IPs to validate through this and 
count them.

##### Day 8: Two-Factor Authentication
Tried to avoid over engineering the screen and instruction processing.
 