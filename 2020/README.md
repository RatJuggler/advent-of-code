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
