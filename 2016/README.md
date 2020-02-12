# advent-of-code 2016

##### Day 1: No Time for a Taxicab
First part is straightforward as we are just updating the current location. For the second part I didn't want to mess around with
trying to find the intersection points of all the direction vectors so I built a history of every point visited and used a stream 
with lambdas to filter and reduce it.

##### Day 2: Bathroom Security
I started off by coding all the state changes explicitly and then refactored this into a more generic solution for defining keypads. 

##### Day 3: Squares With Three Sides
Simple to do in a long handed way but couldn't think of a more elegant/generic approach for Part 2.

##### Day 4: Security Through Obscurity
Problem lends itself to using streams for much of the processing.
 