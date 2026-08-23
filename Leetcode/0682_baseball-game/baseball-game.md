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
