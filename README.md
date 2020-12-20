# advent-of-code

Advent of Code https://adventofcode.com

- 2020 - In progress now using Java 11.

- 2019 - Started using Python (3.x), but not completed. Need to go back and work on this.

- 2018 - Not started. I hope to use Java 11.

- 2017 - Not started. I hope to use Python (3.x).

- 2016 - Completed using Java 11. Certainly seemed a lot harder solving these puzzles in Java rather than Python :thinking:.

- 2015 - Completed using Python (3.x). As I hadn't done much Python at the time it was an excellent learning exercise.

##### Some general points about my solutions:

- I always add the odd test or two rather first, rather than going straight for the answer, to prove I'm on the right track. These 
  are just simple methods using *assert* statements rather than completely separate tests (Note: For Java this means turning on 
  asserts in the JVM with -ea).

- Some of my solutions may seem over-engineered for what is required, but I like to try to create generic solution whilst also 
  keeping in mind how I would be implementing these for a production environment.

- In a similar vein, I often prefer to leave complex processing or calculations unrolled rather than cramming everything onto one 
  line. This does go against the idea of making code compact and succinct but I value being able to see the discreet logical steps.

- I try not to pull in additional libraries beyond those of the core language.

- I've probably overused regular expressions for parsing instead of simple string splits.

- For Python I like to use type hints on function parameters and return types to help clarify the intention of the function.

See each year for more comments on the days puzzles.
