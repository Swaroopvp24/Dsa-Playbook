# baseball-game

## standard_stack_solution.java
*Style: concise*

### Study Notes: Baseball Game Score Calculator

#### Summary
This code calculates the final sum of scores in a baseball game based on a sequence of operations. It uses a stack to dynamically track valid scores, applying rules for addition, doubling, and removal of the previous record.

#### Key Components
*   **`Deque<Integer> stack`**: Stores the sequence of valid points to enable LIFO access for operations.
*   **`+` Operation**: Calculates the sum of the top two elements and pushes it back onto the stack.
*   **`D` Operation**: Doubles the value of the most recent score and pushes the result.
*   **`C` Operation**: Removes the most recent score from the stack.
*   **Default (Integer)**: Parses the string and pushes the raw integer value.

#### Non-Obvious Logic
*   **`+` Operation Order**: When performing the sum, the current top element must be popped (`n1`), the second-to-top must be peeked (`n2`), and then `n1` must be **pushed back** before the sum is added to preserve the stack state.
*   **Summation**: Since the stack stores the final sequence, a simple `while(!stack.isEmpty())` loop is sufficient to aggregate the total result, effectively emptying the stack in the process.

---

## standard_stack_solution2.java
*Style: concise*

### Notes: Baseball Game Score Calculator

**Overview**
This code tracks scores in a baseball game by processing a series of operations (`+`, `D`, `C`, or integer) stored in a `Deque`. It maintains a running total of the record while using the stack to reference historical scores needed for multi-step operations.

**Key Components**
*   `Deque<Integer> stack`: Tracks valid scores to support relative operations (`+`, `D`, `C`).
*   `int res`: Tracks the cumulative sum of all current scores to avoid redundant stack iteration at the end.

**Logic Notes**
*   **"+" operation**: Requires popping the last element to access the second-to-last, then pushing both back. Ensure the sequence `(top + secondTop)` is pushed back onto the stack to maintain the correct state for future operations.
*   **"C" operation**: Simple undo functionality; subtract the popped value from the running total `res` immediately.
*   **Efficiency**: By maintaining `res` throughout the loop, the final result is calculated in $O(N)$ time with $O(N)$ space, avoiding a final $O(N)$ summation pass.

---
