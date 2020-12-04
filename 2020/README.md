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
