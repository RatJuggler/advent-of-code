# advent-of-code 2015

##### Day 1
Very straightforward parsing characters from a string. Didn't see the need to make a generic solution to both parts.

##### Day 2
Again straightforward using separate calculations for each part.

##### Day 3
A chance to use one of the [Itertools Recipes](https://docs.python.org/3/library/itertools.html). Also defined a type
alias to improve readability.

##### Day 4
Used MD5 functionality from hashlib library. Experimented with how to represent a DO...UNTIL type loop in Python. 

##### Day 5
Common processing loop for both steps using lambdas. Explicit functions for each password check to improve readability.
Could have been used to test individual checks if they were more complex.  

##### Day 6
Over-engineered with classes to represent the instruction types (using inheritance) and the light grid itself. Room for
improvement.

##### Day 7
Used classes again to represent the instructions and circuit. Does have a circular dependency but solution seems cleaner
than Day 6.

##### Day 8
Created Santa's list as class wrapping a plain Python *List* to encapsulate the functionality required. Should also 
note use of *eval*.

##### Day 9
A variation on the traveling salesman problem but simple enough to brute force solutions and track multiple variations
of the solutions. Room for improvement though.

##### Day 10
Straightforward parsing puzzle.

##### Day 11
Another straightforward parsing and character manipulation puzzle.

##### Day 12
Easy in Python because the input string can be converted directly to valid Python data types *list* and *dict* using 
the *eval* function.

##### Day 13
I thought this was going to be a variation on the old dinning philosophers problem but its about the seating 
permutations. Started using classes but soon dropped them in favour of just using the standard *dict* type. Room for
improvement as it doesn't take into account the circular nature of the permutations so it produces duplicate solutions.

##### Day 14
Reindeer details lend themselves to being stored as class instances but though it does produce the correct solutions I 
don't think I've got the concept of racing them together quite right. Must revisit.

##### Day 15
Ingredients lend themselves to bring stored as class instances with attributes that can be accessed dynamically using 
*getattr*. Brute force search through the permutations.

##### Day 16
Used a class instance for each Aunt Sue with a *dict* of their variable attributes. Combined with a *dict* of the
search condition lambda functions and a *for* *else* made the processing loop really neat.  

##### Day 17
Another straightforward puzzle where *Itertools* does the heavy lifting. 

##### Day 18
Better implementation of a light grid class but not happy with the way it's animated. Would be interesting to actually
show the animation changes using a *curses* library or similar.

##### Day 19
The first part of the puzzle if fairly straightforward using a *set* to determine the unique molecules but I'm stuck on
the second part. **OUTSTANDING** 

##### Day 20
This is about finding the factors of a number. I forgot to use the square root at first and so my solutions ran very
slowly.

##### Day 21
Had to go overboard and create classes for items and characters but resisted taking it further. Used a lambda to define
the winning conditions for each part.

##### Day 22
**TO BE COMPLETED**

##### Day 23
A chance to build a small op-code interpreter.

##### Day 24
I found this to be the hardest puzzle of all. I didn't think enough early on about cutting down the number of
combinations.

##### Day 25
