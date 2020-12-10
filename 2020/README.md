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
