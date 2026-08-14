# first-missing-positive

## attempt_1.java
*Style: concise*

### Study Notes: First Missing Positive

**Summary**
Finds the smallest missing positive integer in an unsorted array in $O(n)$ time and $O(1)$ space using cyclic sort. It places each number $x$ (where $1 \le x \le n$) into its correct index ($x-1$) by swapping elements in-place.

**Key Components**
*   `swap(int[] nums, int a, int b)`: Utility method to perform an in-place swap of two array elements.
*   `firstMissingPositive(int[] nums)`: Main logic that performs the cyclic sort followed by a linear scan to identify the first index $i$ where `nums[i] != i + 1`.

**Logic Notes**
*   **Cyclic Sort Strategy:** The `while` loop is critical; it ensures that the element at `i` is swapped repeatedly until it is either out of range ($>n$ or $\le 0$) or placed in its "correct" index (`nums[i] - 1`). 
*   **Why `while` instead of `if`?** A simple `if` is insufficient because the swapped element brought into `nums[i]` might also need to be moved to its own correct position.
*   **Termination:** After sorting, the first index $i$ that does not contain $i+1$ is the answer. If all indices $0 \dots n-1$ contain their respective $1 \dots n$, the missing number is $n+1$.

---
