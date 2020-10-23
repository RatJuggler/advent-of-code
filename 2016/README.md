# advent-of-code 2016

##### Day 1: No Time for a Taxicab
First part is straightforward as we are just updating the current location. For the second part I didn't want to mess around with
trying to find the intersection points of all the direction vectors, so I built a history of every point visited and used a stream 
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

##### Day 9: Explosives in Cyberspace
Started of with Part 1 by actually creating the decompressed sequences instead of merely working out their length. Had to backtrack
and refactor this to make Part 2 work. Took the opportunity to try out using methods as parameters.

##### Day 10: Balance Bots
I spent a while on the development before realising that the bots and bins form a tree structure, with the bots as branches, and 
the bins as leaves. Despite this insight I feel I've not produced anywhere near a good solution. It just feels too clunky, this may
in part be due to over-engineered OO Java. Having said that, the comprehensive code created for Part 1 meant that the Part 2 
solution was trivial.

##### Day 11: Radioisotope Thermoelectric Generators
Easily the hardest Advent-of-Code day I have encountered so far! Haven't encountered a problem like this for many, many years.
It should have been a straightforward breadth first search, but I struggled over several days to solve it. Part of the issue was
that I started down the path of trying to generate and validate new states in a single method. While doing this I didn't properly 
implement the validation check for the floor components were being move from, so the rest of my solution suffered. Choosing how to 
represent the problem state was also something I could have done better. Trying to keep things simple I ended up using a single 
String to represent each floor, which seemed to work out in the end but obviously impacted the performance of the Part 2 solution.
On the plus side I did get to use a PriorityQueue which I don't remember using previously.   

##### Day 12: Leonardo's Monorail
Implementation of a simple VM to execute the bunnycode. Except I managed to fall into the trap of not checking that the first 
argument on the JNZ instruction, which can be a number or a register. The problem outline omits this detail, so I assumed it would
always be a register to start with.

##### Day 13: A Maze of Twisty Little Cubicles
This is a maze generation / solving problem. I was able to find the solutions without any issues. I generated a representation of
the mazes but didn't show the paths found.

##### Day 14: One-Time Pad
No issues with this problem. I implemented a cache to hold the 1000 hashes so but didn't look for any other optimisations.

##### Day 15: Timing is Everything
Started to simulate each disc turning then realised you can just test for each slot being aligned with a simple mod check.

##### Day 16: Dragon Checksum
Very straightforward, I reproduced all the examples as tests to ensure the correct result. Though it's always hard to know what
to do with global/common functions in Java when you don't want everything to be part of a class.

##### Day 17: Two Steps Forward
Combining ideas from previous days proved to make this a fairly easy puzzle. I used a BiFunction (functional interface) to pass the
path length check so that it could be swapped from the shortest to the longest length while using the same core path search code.

##### Day 18: Like a Rogue
While the puzzle was straightforward I wasn't happy with my TrapDector class until I'd simplified it down to the bare bones of the
requirement.

##### Day 19: An Elephant Named Joseph
So I worked out the first part simply enough using brute force to remove losing elves in an array of booleans. My attempts with the
second part using a ```LinkedList``` have so far been unsuccessful as they take too long to complete. Swapping back to an array
improves the speed but I'm not getting the correct result.
Looking at the solutions threads these are a variation on the [Josephus Problem](https://en.wikipedia.org/wiki/Josephus_problem).

##### Day 20: Firewall Rules
Fairly straightforward though I'm sure there's a better way of consolidating the ranges than my implementation.

##### Day 21: Scrambled Letters and Hash
This one requires extra careful reading to ensure that the Part 1 scrambling rules were implemented correctly. To help with this I 
split out the various transformations into their own class and added tests for them. Part 2 looked like it should be similar, just
with unscrambling rules, but I've got stuck trying to implement the reverse of the "rotate based on letter position" instruction. 

##### Day 22: Grid Computing
I completed Part 1 without implementing the full Node connectivity to keep things simple. I've decided to move on instead of
tackling Part 2 as this time. 

##### Day 23: Safe Cracking
Reusing and refining the code from Day 12 to add a new instruction for Part 1 was fun. I implemented the optimisation without any
major refactoring of the existing code, so it seemed a bit clunky, but works fine.
