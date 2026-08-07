# longest-consecutive-sequence

## attempt_1_hashSet.java
*Style: detailed*

# Technical Deep Dive: Longest Consecutive Sequence

## 1. Summary
The algorithm solves the Longest Consecutive Sequence problem using a **HashSet-based deduplication and anchor-point strategy**. The core objective is to identify the longest contiguous integer sequence in an unsorted array in linear time.

Instead of sorting the array ($O(n \log n)$), the approach treats the input as a set to achieve $O(1)$ average-time lookups. By identifying "sequence starters" (elements $x$ where $x-1$ is not present in the set), the algorithm ensures that each sequence is traversed exactly once from its minimum element, effectively pruning redundant computations.

## 2. Complexity Analysis

### Time Complexity: $O(n)$
*   **Set Construction:** Building the `HashSet` takes $O(n)$ on average, as each of the $n$ elements involves a hash calculation and insertion.
*   **Sequence Traversal:** While there is a `while` loop nested inside a `for` loop, the inner loop only executes for elements that are the *start* of a sequence. 
*   **Amortized Analysis:** Each element in the set is visited at most twice: once by the outer loop iterator and once by the `while` loop (or checked as a `num - 1` condition). Consequently, the total number of operations scales linearly with $n$.

### Space Complexity: $O(n)$
*   The `HashSet` stores up to $n$ unique integers from the input array. In the worst case (all elements are unique), the space requirement is linear relative to the input size.

## 3. Component Deep Dive

### Data Structure: `java.util.HashSet`
The `HashSet` is critical here for constant-time complexity. It provides the necessary interface to verify the existence of neighbors ($num \pm 1$) without performing a full scan of the array.

### The "Anchor" Logic (Pruning)
```java
if (un.contains(num - 1)) continue;
```
This is the most crucial architectural decision in the code. By checking if `num - 1` exists, we force the inner `while` loop to trigger **only** for the smallest element of any potential sequence. 
*   If `num - 1` exists, `num` is an internal node of a sequence; we skip it because it will be processed (or has already been processed) as part of a sequence starting at a smaller value.
*   This constraint prevents $O(n^2)$ behavior in cases of long consecutive sequences.

### Traversal and Counter
```java
while (un.contains(cur++)) curC++;
```
The use of the post-increment operator (`cur++`) inside the `contains` check is an elegant way to maintain the count and increment the pointer simultaneously. Note that `cur` will be one greater than the last element of the sequence when the loop terminates, which is handled correctly by the `curC` count increment.

## 4. Key Insights

### Performance Nuances
*   **Hash Collisions:** The $O(n)$ complexity assumes a good distribution of hash codes. In a scenario with pathological hash collisions, `HashSet` operations could degrade to $O(n)$, making the overall algorithm $O(n^2)$. Using the default `Integer` wrapper is generally safe due to its identity hash mapping, but in environments with custom objects, this is a potential bottleneck.
*   **Memory Overhead:** `HashSet<Integer>` is memory-heavy compared to a primitive array. Each `Integer` object is an entry in the map containing an `Entry` object, a key, and a hash value. For massive datasets, this might trigger frequent Garbage Collection (GC) cycles.

### Edge-Case Handling
*   **Empty Input:** If `nums` is empty, the `HashSet` remains empty. The outer loop will not execute, and `ans` will correctly return `0`.
*   **Duplicates:** The code naturally handles duplicates by utilizing `Set.add()`, which performs de-duplication. The length calculation remains accurate because redundant values simply vanish into the set.
*   **Single Element:** If `nums` contains one element, the `while` loop iterates exactly once, `curC` becomes 1, and the function returns 1.

### Subtle Considerations
*   **Integer Overflow:** The current logic is safe from `cur++` overflow as long as the sequence length does not hit `Integer.MAX_VALUE`. Given the constraints of typical competitive programming platforms, this is rarely an issue, but for sequences spanning the full range of `Integer`, one should be aware of overflow wrapping.

---
