# CSC-454 HW4B
```
This is your first homework on discrete event simulation. We will use a slightly modified version of the vending machine, since that is already familiar to you.

Implement and simulate an atomic model of a vending machine. The machine sells coffee and accepts only nickels, dimes, and quarters. When $1 has been inserted, the machine dispenses a cup of coffee. The machine accepts one coin at a time (note the difference from last time), and does not have a cancel button. If there is no input to the machine for two seconds after a previous input, it dispenses coffee(s) (if the value of the entered coins is sufficient), and also returns change.

The machine has four state variables: q, n, and d represent the number of quarters, nickels, and dimes the machine has in storage, and v represents the total value that the user has entered so far.

The input set of the machine is X = {q, n, d}. The machine only accepts one coin at a time.

The output set of the machine is Y = {coffee, q, n, d}. Note that a particular output value is a bag of the members of Y.

The time advance function is ta({q, n, d, v}) = 2.0 if v > 0, and infinity otherwise.

The output function is lambda({q, n, d, v}) = {coffee for every 100 cents in v} union {a suitable combination of q, n, d representing v % 100}.

The internal state transision function is deltaint({q, n, d, v}) = {q-x, n-y, d-z, 0} where x, y, and z are a suitable combination of coins representing the previous value of v % 100 (i.e., the change that was output to the user is subtracted from the number of stored coins).

The external state transision function is deltaext({q, n, d, v}, e, x) =

{q+1, n, d, v+25} if x == q
{q, n+1, d, v+5} if x == n
{q, n, d+1, v+10} if x == d
The confluent state transition function is deltacon({q, n, d, v}, x) = deltaext(deltaint({q, n, d, v}), 2.0, x). This simply means that we have no special handling for the confluent case, and do the obvious: apply the internal function, then apply the external function. Note that this is ONE step in the total delta function, so there is no time advance between the application of deltaint and deltaext.
```
