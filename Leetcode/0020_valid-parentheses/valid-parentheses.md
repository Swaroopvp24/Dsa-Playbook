# valid-parentheses

## attempt_1.java
*Style: concise*

### Study Notes: Valid Parentheses (Array-based Stack)

**Overview**
This solution validates balanced parentheses by using a fixed-size character array as a stack. It achieves $O(n)$ time complexity by pushing the *expected* closing character onto the stack whenever an opening bracket is encountered.

**Key Components**
*   **`char[] stack`**: Pre-allocated array acting as the stack; avoids the overhead of `java.util.Stack`.
*   **`int top`**: Pointer tracking the index of the last pushed character (`-1` indicates an empty stack).
*   **Main Loop**: Iterates through the input string once, performing constant-time push/pop operations.

**Non-Obvious Logic**
*   **Inverse Mapping**: By pushing the *expected closing bracket* (`')'`, `']'`, `'}'`) onto the stack during an opening bracket encounter, the comparison logic for closing brackets is simplified to a single equality check (`stack[top--] != c`).
*   **Early Exit**: The `s.length() % 2 != 0` check is a cheap optimization for strings that cannot possibly be balanced.
*   **Empty Stack Safety**: The condition `top == -1` inside the `else` block handles cases like `")"` or `"]]"`, preventing `ArrayIndexOutOfBoundsException` while correctly identifying premature closing brackets.
*   **Final State**: The check `return top == -1` ensures that no unclosed brackets remain in the stack (e.g., `"(("` would leave `top` at `1`).

---
