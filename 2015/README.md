# advent-of-code 2015

##### Day 1: Not Quite Lisp
Very straightforward parsing characters from a string. Didn't see the need to make a generic solution to both parts.

##### Day 2: I Was Told There Would Be No Math
Again straightforward using separate calculations for each part.

##### Day 3: Perfectly Spherical Houses in a Vacuum
A chance to use one of the [Itertools Recipes](https://docs.python.org/3/library/itertools.html). Also defined a type
alias to improve readability.

##### Day 4: The Ideal Stocking Stuffer
Used MD5 functionality from hashlib library. Experimented with how to represent a DO...UNTIL type loop in Python. 

##### Day 5: Doesn't He Have Intern-Elves For This?
Common processing loop for both steps using lambdas. Explicit functions for each password check to improve readability.
Could have been used to test individual checks if they were more complex.  

##### Day 6: Probably a Fire Hazard
Over-engineered with classes to represent the instruction types (using inheritance) and the light grid itself. Room for
improvement.

##### Day 7: Some Assembly Required
Used classes again to represent the instructions and circuit. Does have a circular dependency but solution seems cleaner
than Day 6.

##### Day 8: Matchsticks
Created Santa's list as class wrapping a plain Python *List* to encapsulate the functionality required. Should also 
note use of *eval*.

##### Day 9: All in a Single Night
A variation on the traveling salesman problem but simple enough to brute force solutions and track multiple variations
of the solutions. Room for improvement though.

##### Day 10: Elves Look, Elves Say
Straightforward parsing puzzle.

##### Day 11: Corporate Policy
Another straightforward parsing and character manipulation puzzle.

##### Day 12: JSAbacusFramework.io
Easy in Python because the input string can be converted directly to valid Python data types *list* and *dict* using 
the *eval* function.

##### Day 13: Knights of the Dinner Table
I thought this was going to be a variation on the old dinning philosophers problem but its about the seating 
permutations. Started using classes but soon dropped them in favour of just using the standard *dict* type. Room for
improvement as it doesn't take into account the circular nature of the permutations so it produces duplicate solutions.

##### Day 14: Reindeer Olympics
Reindeer details lend themselves to being stored as class instances but though it does produce the correct solutions I 
don't think I've got the concept of racing them together quite right. Must revisit.

##### Day 15: Science for Hungry People
Ingredients lend themselves to bring stored as class instances with attributes that can be accessed dynamically using 
*getattr*. Brute force search through the permutations.

##### Day 16: Aunt Sue
Used a class instance for each Aunt Sue with a *dict* of their variable attributes. Combined with a *dict* of the
search condition lambda functions and a *for* *else* made the processing loop really neat.  

##### Day 17: No Such Thing as Too Much
Another straightforward puzzle where *Itertools* does the heavy lifting. 

##### Day 18: Like a GIF For Your Yard
Better implementation of a light grid class but not happy with the way it's animated. Would be interesting to actually
show the animation changes using a *curses* library or similar.

##### Day 19: Medicine for Rudolph
The first part of the puzzle if fairly straightforward using a *set* to determine the unique molecules but I got really
stuck on the second part. In the end I had to look on the support [reddit thread](https://www.reddit.com/r/adventofcode/comments/3xflz8/day_19_solutions/)
for hints on how to solve it.

##### Day 20: Infinite Elves and Infinite Houses
This is about finding the factors of a number. I forgot to use the square root at first and so my solutions ran very
slowly.

##### Day 21: RPG Simulator 20XX
Had to go overboard and create classes for items and characters but managed to resist taking it further. Used a lambda
to define the winning conditions for each part.

##### Day 22: Wizard Simulator 20XX
Thought this one was going to be straightforward, similar to Day 21, but took me ages to get the code right for the
correct solutions. I had to use a solution from the support [reddit thread](https://www.reddit.com/r/adventofcode/comments/3xspyl/day_22_solutions/cy7mbfz/)
to check what the answer should be and use that to work through where my code was going wrong. I think my main problem
was insisting on duplicating the output shown in the examples as part of the test and trying to produce a generic fight
simulator rather than just a bare bones solution.

##### Day 23: Opening the Turing Lock
A chance to build a small op-code interpreter.

##### Day 24: It Hangs in the Balance
I found this hard going to begin with because I didn't think enough early on about cutting down the number of 
combinations. Also, as a variation on the knapsack problem it would likely have been easier to use something like 
[Google OR-Tools](https://developers.google.com/optimization/bin/multiple_knapsack) to solve it

##### Day 25: Let It Snow
Straight-forward once you've worked out an algorithm on how the codes are placed in the sheet. I implemented a generic 
solution to better illustrate this.
