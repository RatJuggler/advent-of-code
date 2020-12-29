# advent-of-code 2020

##### Day 1: Report Repair
So this looks like a Knapsack type problem but just using loops with some shortcuts solved both parts with no delay. I wouldn't
trust my solutions on a larger problem space without further testing though.

##### Day 2: Password Philosophy
Some simple parsing and checks, but I spent longer refactoring to produce generic code usable for both parts of the problem. It's
very Java with a factory 🤦.

##### Day 3: Toboggan Trajectory
A fairly straightforward simulation except my solution on part 2 was incorrect at first because I was calculating the product as 
an int and not a long.

##### Day 4: Passport Processing
I completed the fist part fairly quickly but moving on to the second part I immediately got lost in a twisty little maze of Java 
objects as I tried to produce a generic solution. Seeing this I determined the part two solution with what I had and then set about
refactoring again. My big mistake was starting with inheritance and once I'd refactored that to composition a generic solution
fell more easily into place. Always favour composition over inheritance.

##### Day 5: Binary Boarding
Solved part 1 as a simulation first and then refactored to generate the solution directly from the boarding seat code. The code
looks simpler, but I'm not sure it is any quicker in Java than the simulation with all the character replacement going on. Not 
entirely happy with my part 2 solution but a loop seems the only way of finding the missing sequence number in Java. 

##### Day 6: Custom Customs
I spent some time looking at solving this with RegEx but couldn't see how so went with solutions using Java streams instead. For
extra practice I used lambda's with a standard functional interface to implement the two core methods. 

##### Day 7: Handy Haversacks
I found this much harder than the previous days. After spending some time on parsing and storing the bag rules I had made slow 
progress on the part 1 solution. I thought recursion would produce the best solution, but I ended up with a loop. Part 2 was much
easier, and I had no problems with a recursion solution.

##### Day 8: Handheld Halting
I love these VM/emulator type problems. Probably an over the top implementation but there might be a chance of reuse knowing how 
previous Advent Of Code's go.

##### Day 9: Encoding Error
Reusing some ideas from day 1. After finding the solutions I spent some time on refactoring, which reduced the amount of code, but 
I'm not sure resulted in any performance improvements.

##### Day 10: Adapter Array
So the first part was fairly easy, the second part not-so-much. For part two I wrote out the possible arrangements to look for, 
then used a simple loop to detect them and accumulate the result. After using steams for most of the processing this doesn't look 
very elegant but gets the job done.

##### Day 11: Seating System
Game of life type simulation. I tried to keep things simple by using a char array, instead of Lists/Strings, but Java doesn't like 
streaming those so there is some converting back forth.

##### Day 12: Rain Risk
I ended doing exactly what I did on Day 4 again. Completed the first part quickly and without issue, then got side tracked 
faffing about with how best to implement a combined solution for both parts and getting nowhere. Of course once I'd actually 
bothered to generate a solution I was able to go back and refactor it into what I was looking for.

##### Day 13: Shuttle Search
My original code for part one wasn't very elegant to start with, but I managed to knock it into shape with some refactoring after 
generating the solution. Looking at part two it was immediately obvious that something other than brute force would be required to
generate a solution in a reasonable timeframe but at this point I was completely stuck. I spent some time refactoring, to eke a bit 
more performance out of my brute forcing and to see if I could gain any insights on how to proceed, but didn't come up with 
anything. Taking a peek at the #AdventOfCode tag on Twitter and people were mentioning the Chinese Remainder Theorem. I Googled 
this but after a quick read decided that the time it would take me to understand it was more than I wanted to spend at this moment. 
Instead, I kicked off the brute force solution on my desktop PC (i7-6700K@4.6GHz), and it produced a result within a couple of 
hours (I didn't time it). I will revisit this at some point to implement a proper solution. 

##### Day 14: Docking Data
My only issue solving today's problem was using a primitive integer array of arbitrary size for the memory. Whilst this worked for 
the first part it failed completely with the second as the memory addresses to be accessed far exceeded the maximum array size. It 
was also wasting memory as only comparatively few addresses were actually being used. In the end I used a map as a kind of sparse 
array.

##### Day 15: Rambunctious Recitation
Didn't spend long on this. Made some minor improvements to speed it up but didn't consider other solutions than brute force.

##### Day 16: Ticket Translation
Java to the maximum! I liked this one's mix of parsing and puzzle and ended up creating six additional classes whilst solving it.

##### Day 17: Conway Cubes
I completed this, but I wasn't happy with my solution, so I went back and completely rewrote it. I'd started by trying to simulate
an arbitrarily large volume of space but this was very inefficient and unwieldy. Just working with a list of the cube positions, 
and the offset positions they affect is actually much simpler, the hardest part is actually generating the offset combinations to 
try. Further, refactoring eliminated duplicate checks being carried on the same location, much improving performance.

##### Day 18: Operation Order
Took me a while to get my expression parser going properly using a tree structure but got there in the end. Certainly room for 
improvement as I could be evaluating sub-expressions as they are parsed, rather than building the whole tree and then evaluating.

##### Day 19: Monster Messages
I got the match rules going for part one with my usual tangle of Java classes and input file parsing, though it doesn't feel quite 
right. Something about the way the rules and the message parsing context interact. 
** Part Two not completed ** I'm having problems understanding the implications of the rule changes in part two.

##### Day 20: Jurassic Jigsaw
No progress.

##### Day 21: Allergen Assessment
Had to restart this after the example didn't turn out to be as close to what was required with the actual input, but I got there
in the end. Another chance to practice with Java streams and `Set` intersections with `retainAll`. 

##### Day 22: Crab Combat
I managed to determine a solution for part one using rather too generic code with lots of streams. After working on part two for a 
while it became apparent this was overly complicated to work with, so I refactored to a simpler style.
** Part Two not completed ** Despite this I've not been able to crack part 2. 

##### Day 23: Crab Cups
No progress.

##### Day 24: Lobby Layout
Not started.

##### Day 25: Combo Breaker
Not started.
