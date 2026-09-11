# eating-bananas

## standard_binary_search.java
*Style: detailed*

# Engineering Deep-Dive: Koko Eating Bananas (Binary Search on Answer)

## 1. Summary
The problem is a classic application of **Binary Search on the Solution Space**. Instead of searching for an element within a data structure, we search for the optimal integer $k$ (eating speed) within the range $[1, \max(piles)]$. The monotonicity of the problem is key: if Koko can finish all piles at speed $k$, she can also finish them at any speed $> k$. If she cannot finish at speed $k$, she cannot finish at any speed $< k$. This allows us to treat the eating speed requirement as a monotonic function, enabling logarithmic time complexity.

## 2. Complexity Analysis

### Time Complexity: $O(N \log M)$
*   **$N$**: The number of piles.
*   **$M$**: The maximum number of bananas in a single pile.
*   **Explanation**: We perform a binary search over the range of possible speeds $[1, M]$. The range size is $M$, leading to $\log M$ iterations. In each iteration, we iterate through the entire `piles` array once to calculate `totalHours`, which takes $O(N)$. Thus, total time is $O(N \log M)$.

### Space Complexity: $O(1)$
*   **Explanation**: We only allocate a constant amount of memory for variables (`maxPileSize`, `left`, `right`, `minimumSpeed`, `totalHours`). The auxiliary space does not scale with the input size.

---

## 3. Component Deep Dive

### The Search Space
The lower bound is fixed at `1` (Koko must eat at least one banana per hour). The upper bound is `maxPileSize`. While one could technically set `right` to the sum of all bananas, `maxPileSize` is a tighter, more optimized bound because eating faster than the largest pile yields no marginal utility (you can only finish one pile per hour at best).

### The Ceiling Division Trick
Calculating `ceil(pile / eatingSpeed)` using standard floating-point division is expensive and risks precision issues. The implementation uses the integer arithmetic equivalent:
```java
(pile + eatingSpeed - 1) / eatingSpeed
```
This is a robust idiomatic pattern for performing a ceiling division using integer truncation. It works because adding `eatingSpeed - 1` effectively "bumps" the numerator to the next multiple of the denominator, causing integer division to behave like a ceiling function.

### Binary Search Invariant
*   **Invariant**: `left` represents the smallest speed tested that might work; `right` represents the largest speed tested that might be too slow.
*   **Convergence**: When `left > right`, the `minimumSpeed` variable holds the smallest value that satisfied the `totalHours <= h` condition during the search.
*   **Edge Case Handling**: 
    *   **$h < piles.length$**: The current logic will return `maxPileSize`, which is technically correct behavior for an impossible time constraint (Koko can't eat faster than 1 pile/hour).
    *   **Integer Overflow**: Note the use of `long totalHours`. Even though the number of piles is typically limited, a large `piles[i]` combined with a small `eatingSpeed` could lead to an accumulation that exceeds `Integer.MAX_VALUE`. Using `long` is a critical defensive programming choice.

---

## 4. Key Insights

### Monotonicity is the "Hook"
The most important takeaway is recognizing that the problem asks for the *minimum* of a value that satisfies a *predicate* (total hours $\le h$). Whenever you see a prompt asking for "minimum speed," "minimum capacity," or "maximum duration" where the feasibility predicate is monotonic, Binary Search on the answer is almost always the optimal approach.

### Efficiency Nuances
*   **Avoiding unnecessary iterations**: Inside the `while` loop, as soon as `totalHours` exceeds `h`, the execution could technically `break` to save cycles, though it does not change the Big O complexity.
*   **Data Type Safety**: While not explicitly shown in the code, if `piles[i]` could be near `Integer.MAX_VALUE`, even `pile + eatingSpeed - 1` might overflow. In extreme edge-case scenarios, one should use `long` arithmetic for the calculation of the hours to ensure complete safety.
*   **Performance Optimization**: Finding `maxPileSize` is an $O(N)$ operation. This is mandatory, as it dictates the width of our search space. There is no way to bypass this initial scan.

---
