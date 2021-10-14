# Dice engine

Table top roleplaying games often require a lot of different dice to play. With
people stuck at home, gamers have taken to the internet to play these games.
However, they need a dice engine to roll different kinds of dice and generate
random numbers.

Thankfully, the dice notation is consistent. It is as follows:

`NdX`

where:
- `N` is the number of dice rolled and 
- `X` is the number of faces those dice each have

# Dice Syntax

### Unary operations (numerical)

| syntax | name     | description         |
|--------|----------|---------------------|
| +X     | positive | does nothing        |
| -Y     | negative | negative value of Y |

### Binary operations (numerical)

To start with, we need to support multiplication, division, addition and
subtraction.

eg.

`1 + 2`
`3d4 + 1`

### Brackets

Brackets override the natural order of precedence.

eg.

`3 + 3 / 3` evaluates to `4`

Whereas,

`(3 + 3) / 3` evaluates to `2`

### Sets

The result of multiple die rolls rolled together in the form of `NdX` should be
polymorphic. They should be treated as a number or a set, depending on the
operations made on it. The final result should show both, the set of values and
the numerical result.

eg. For `3d10` the final result should be of the form

`(5, 2, 7) => 14`

### Literal sets

We need to support literal sets of values, for example:

`(1, 9, 2, 2d4)`

Which can result in a set with values: 1, 9, 2 & 5 (from rolling the 2d4).

### Set operations

| Syntax | Name     | Description                                 |
|--------|----------|---------------------------------------------|
| k      | keep     | keep all matched values                     |
| d      | drop     | Drop all matched values                     |
| rr     | reroll   | Rerolls all matched values until none match.|

The notation is extended by adding an optional set operation followed by a selector.

eg. `3d4k2` would mean, roll 3 4-sided dice and keep all twos. If 2, 3 and 2
are rolled, only the two 2s are kept.

### Set selectors

Set operations are applied with selectors. They act as a filter on the values in a set.

| Syntax | Name | Description |
|--------|------|-------------|
| X | literal | All values that are literally this value |
| hX | highest X | The highest X values in the set |
| lX | lowest X | The lowest X values in the set |
| \>X | greater than X | Values in the set that are greater than X |
| \<X | lesser than X | Values in the set that are lesser than X |

Similarly, `dlZ` is used to keep lowest Z dice. In the previous example,
`3d10kl2` with the same rolls would amount to 3 + 8 => 11.

These are `set operations`. A group of rolls of the form `NdX` can be
considered a set of values.

# Problem statement

Build a dice engine that can read this syntax and return the result
