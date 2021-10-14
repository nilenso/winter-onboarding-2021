# Dice Engine

## Entities
 1. Dice
 2. Set
 3. Opearation
 4. Selector
 5. Expression


### Dice

A Dice must have atleast 1 side, and atleast 1 dice must be rolled.

When a dice is rolled, it gives us a Number.

### Set

A set can contain Numbers, Expressions, Nested Expressions too.

If a Set contains expressions then we can evaluate the expressions inside them.

A Set can be reduced to a Number.

We can apply Operations to Set.

Some Set examples:

(5+2, 1, 3d4k1)

(1, 2, 5)

(-1, 1, (5-4)d(2+2)rr2)



### Operation
An opeartion **needs** a Selector to operate on a Set.

So in expression `(1d4 + 1, 3, 2d6kl1)kh1`: Before applying `kh1` to `(1d4 + 1, 3, 2d6kl1)`, we must reduce the `(1d4 + 1, 3, 2d6kl1)` to a set conatining only Numbers like `(5, 3, 1)`.

3 types of operation:

1. k - keep
2. d - drop
3. rr -- reroll

### Selector
Selectors are solely used by Operations.

These are of types:
1. X
2. lX
3. hX
4. \>X
5. <X


### Expression

An expression is a combination of Dices, Operations, Selectors and nested Expressions.

Some Expression examples:

`(1d4 + 1, 3, 2d6kl1)kh1`

`2d6rr<3`

`16d9rrl8`