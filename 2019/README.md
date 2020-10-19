# advent-of-code 2019

##### Day 1: The Tyranny of the Rocket Equation
Keeping it simple with separate loops for each calculation part.

##### Day 2: 1202 Program Alarm
Defined the Intcode processor as a class and included a test.

##### Day 3: Crossed Wires
My solution is somewhat long-winded, with a whole series of discrete steps, because of how I broke the problem down. I stored each
section of the wires as lines and then worked out all the intersections between them which is a lot of work. On review a better 
solution might have been to just plot the wires into a 2d array simply recording the intersections as they were found.

##### Day 4: Secure Container
I kept all of the password criteria functions separate and made sure that they passed all the examples given. I could then pass the
functions required for each part of the solution into the same validation loop.

##### Day 5: Sunny with a Chance of Asteroids
I spent quite a bit of time on this trying to improve the Intcode processor and included all the tests supplied with the puzzle.
Also, I was surprised to learn that Python doesn't have a ```switch``` statement.
