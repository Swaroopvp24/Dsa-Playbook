# top-k-elements-in-list

## attempt_1_maxHeap.java
*Style: detailed*

# Engineering Deep Dive: Top-K Frequent Elements

## 1. Summary
The provided solution utilizes a **Frequency Map-based Heap approach** to solve the "Top-K Frequent Elements" problem. The algorithm maps input values to their respective frequencies using a `HashMap` and then employs a `PriorityQueue` (max-heap) to sort the unique elements based on their counts.

**Algorithmic Technique:**
1.  **Frequency Counting:** Linear scan with hash-based aggregation.
2.  **Heapification:** Insertion of all unique elements into a max-heap structured by frequency.
3.  **Extraction:** Poll the top $K$ elements from the heap.

---

## 2. Complexity Analysis

### Time Complexity: $O(N + U \log U)$
*   **Counting Phase ($O(N)$):** We iterate through the input array of size $N$ exactly once. Hash map operations (put/get) are $O(1)$ on average.
*   **Heap Population ($O(U \log U)$):** Where $U$ is the number of unique elements. Inserting an element into a heap takes $O(\log U)$. Performing this for all $U$ unique elements results in $O(U \log U)$.
*   **Extraction Phase ($O(K \log U)$):** Removing the top $K$ elements takes $O(K \log U)$.
*   **Total:** Since $U \leq N$, the worst-case complexity is $O(N + U \log U)$.

### Space Complexity: $O(N)$
*   **HashMap:** Stores up to $U$ unique elements.
*   **Heap:** Stores up to $U$ unique elements.
*   **Result:** An array of size $K$.
*   **Total:** $O(U + K)$, effectively $O(N)$ in the worst case (where all elements are unique).

---

## 3. Component Deep Dive

### Frequency Map (`HashMap<Integer, Integer>`)
*   **Functionality:** Serves as the histogram. It decouples the raw input distribution from the sorting logic.
*   **Edge Case Handling:** Handles empty input arrays naturally (returns empty result if $K=0$). If the input contains a single element or all elements are identical, the map correctly returns a single entry.

### Max-Heap (`PriorityQueue`)
*   **Comparator:** Uses `Integer.compare(b[1], a[1])` to implement a max-heap based on frequency.
*   **Note on implementation:** The code provided inserts *all* $U$ unique elements into the heap. While this is functional, it is suboptimal compared to maintaining a heap of size $K$ (or using Bucket Sort for $O(N)$ performance).

---

## 4. Key Insights & Critical Observations

### The "Commented-Out" Logic Trap
The code contains a commented-out block that attempts to cap the heap size at $K$:
```java
// if(maxHeap.size() > k){
//     maxHeap.poll();
// }
```
**Critical Bug:** This logic is incompatible with a **Max-Heap**. If you use a max-heap and poll when the size exceeds $K$, you remove the *most frequent* elements, effectively leaving the *least frequent* elements. 
*   To keep the heap size at $K$, you must use a **Min-Heap** (by reversing the comparator: `(a, b) -> Integer.compare(a[1], b[1])`). This ensures that the smallest frequencies are evicted when the heap grows beyond $K$.

### Performance Nuances
1.  **Heap vs. Bucket Sort:** For large datasets, the $O(N + U \log U)$ complexity is acceptable. However, one can achieve **$O(N)$** using **Bucket Sort** (creating an array of lists where index = frequency). Since frequencies are bounded by the size of the input array ($N$), bucket sort is strictly superior for this specific problem.
2.  **Memory Overhead:** `new int[]{entry.getKey(), entry.getValue()}` creates a new object for every unique element. In a high-throughput system with many unique integers, this leads to significant garbage collection pressure.
3.  **Comparator Efficiency:** The `Integer.compare` call is appropriate. Avoid manual subtraction (e.g., `b[1] - a[1]`) as it is susceptible to integer overflow bugs if frequencies were stored in larger types or manipulated differently.

### Recommendations for Production
*   **Constraint Validation:** If $K$ is close to $U$, the heap approach is fine. If $K$ is small, use the Min-Heap strategy to reduce space complexity to $O(K)$.
*   **Input Validation:** The code assumes $K$ is valid. Add a check `if (k == 0 || nums == null)` to prevent `NullPointerException` or unexpected array sizing.

---

## attempt_2_minHeap.java
*Style: detailed*

# Engineering Reference: Top K Frequent Elements

## 1. Summary
The solution employs a **Frequency Map + Min-Heap** approach to solve the Top-K problem. By decoupling the counting process from the selection process, the algorithm transforms an $O(N \log N)$ sorting problem into an $O(N \log K)$ selection problem. The Min-Heap serves as a sliding window of size $K$ that maintains the highest frequency elements encountered so far, effectively evicting the "least frequent of the top-K" elements whenever the heap capacity is exceeded.

---

## 2. Complexity Analysis

### Time Complexity: $O(N \log K)$
*   **Counting Phase ($O(N)$):** We iterate through the input array once to populate the `HashMap`. Each insertion/update is $O(1)$ on average.
*   **Heap Phase ($O(M \log K)$):** We iterate over the $M$ unique elements in the map. For each element, we perform an `offer` and potentially a `poll` operation on the heap. Since the heap size is capped at $K$, each operation is $O(\log K)$.
*   **Total:** $O(N + M \log K)$. In the worst case where $M \approx N$, this simplifies to **$O(N \log K)$**.

### Space Complexity: $O(N)$
*   **Frequency Map ($O(M)$):** Stores up to $M$ unique elements where $M \le N$.
*   **Heap ($O(K)$):** The heap contains exactly $K$ elements at the steady state.
*   **Total:** **$O(N)$** space to store the frequency mapping.

---

## 3. Component Deep Dive

### Frequency Mapping (`HashMap<Integer, Integer>`)
*   **Purpose:** Aggregates element counts.
*   **Constraint Handling:** The use of `getOrDefault(n, 0) + 1` is standard. For massive datasets, one might consider a primitive map (e.g., fastutil or Koloboke) to avoid `Integer` object overhead and boxing/unboxing latency.

### Min-Heap (`PriorityQueue`)
*   **Mechanism:** Initialized with a custom comparator `(a, b) -> Integer.compare(a[1], b[1])`.
*   **Why Min-Heap?** A Min-Heap of size $K$ ensures that the root of the heap is always the element with the *smallest frequency among the top-K candidates*. When a new element is processed that has a higher frequency than the current root, the root is discarded (`poll`). This maintains the "Top K" invariant efficiently.

### Edge Case Handling
*   **$K = N$:** The heap will store all unique elements. The algorithm gracefully handles this, though performance degrades to $O(N \log N)$ because the heap grows to $N$.
*   **Input Array size $1$:** The code correctly handles single-element arrays with no heap overflow logic trigger.
*   **Tie-breaking:** The current implementation doesn't specify behavior for identical frequencies. Given the nature of `PriorityQueue`, the eviction order for equal frequencies is non-deterministic (based on heap structure), which is generally acceptable for this problem definition.

---

## 4. Key Insights

### Performance Optimization: The "Bucket Sort" Alternative
While the $O(N \log K)$ approach is robust, it can be optimized to **$O(N)$** using a **Bucket Sort (Frequency Array)** strategy:
1. Create an array of lists `List<Integer>[] buckets = new List[nums.length + 1]`.
2. Map frequencies to indices in the bucket array (where `index` is the frequency).
3. Iterate from the back of the bucket array to pick the top $K$ elements.
*   *Trade-off:* The bucket sort approach improves time complexity to linear time but requires $O(N)$ additional space for the bucket structures, which can be memory-intensive if the range of frequencies is sparse.

### Implementation Nuances
*   **Memory Pressure:** Using `new int[] {entry.getKey(), entry.getValue()}` creates $M$ small objects. In memory-constrained environments, this can trigger frequent GC cycles.
*   **Generics:** The `PriorityQueue<int[]>` approach is clean but involves primitive boxing within the array. Ensuring the heap comparator strictly uses index `[1]` (the frequency) is critical. If one were to sort by value (`[0]`) by mistake, the logic would collapse into an arbitrary selection rather than frequency-based.
*   **Stability:** If the problem required preserving the relative order of elements with the same frequency (e.g., "return elements in order of appearance"), this specific `PriorityQueue` implementation would require an additional metadata field to track original indices, as the current structure loses the arrival sequence.

---

## BucketSort.java
*Style: detailed*

# Engineering Deep Dive: Bucket Sort Approach for Top-K Frequent Elements

## 1. Summary
The provided solution utilizes the **Bucket Sort** (or Distribution Sort) algorithm to achieve $O(n)$ time complexity, bypassing the $O(n \log n)$ or $O(n \log k)$ overhead associated with traditional sorting or heap-based solutions.

The strategy maps the frequency of elements (the value) to the index of a pre-allocated array of lists (the bucket). Since the maximum possible frequency of any element is $n$ (the length of the input array), an array of lists of size $n+1$ is sufficient to group all elements by their respective occurrences. We then traverse this array in reverse (highest frequency first) to extract the top $k$ elements.

---

## 2. Complexity Analysis

### Time Complexity: $O(n)$
*   **Counting:** Iterating through the input array to populate the `HashMap` takes $O(n)$.
*   **Bucketing:** Iterating through the $m$ unique elements in the `HashMap` to populate the `freq` array takes $O(m)$, where $m \leq n$.
*   **Collection:** Traversing the `freq` array takes $O(n)$ because the total number of elements across all buckets is exactly $n$.
*   **Total:** $O(n + m + n) \approx O(n)$.

### Space Complexity: $O(n)$
*   **HashMap:** Stores $m$ unique elements, taking $O(m)$ space.
*   **Bucket Array:** The `freq` array is size $n+1$. In the worst case (all unique elements), the list overhead and object storage result in $O(n)$ space.
*   **Total:** $O(n)$.

---

## 3. Component Deep Dive

### The `freq` Array: `List<Integer>[] freq = new List[n + 1]`
This is a **Frequency Distribution Map**. 
*   **Why $n+1$?** An element can appear anywhere from 1 to $n$ times. Index 0 is ignored (as an element cannot appear 0 times in the input), and indices $1 \dots n$ represent the frequency count.
*   **Design Choice:** Using an array of lists handles potential "collisions" where multiple distinct numbers have the exact same frequency. The `List` effectively acts as a bucket for those ties.

### Implementation Nuances
1.  **Initialization:** The loop `for (int i = 0; i <= n; i++) freq[i] = new ArrayList<>();` is critical. In Java, an array of generic types (or `List`) is initialized with `null` values. Attempting to add an element to `freq[i]` without explicit instantiation will result in a `NullPointerException`.
2.  **Reverse Traversal:** The collection phase `for (int i = freq.length - 1; i > 0; i--)` naturally enforces the "top" requirement. By iterating from the highest index downwards, we ensure that higher frequency items are processed before lower frequency ones.
3.  **Early Exit:** The `if (index == k) return res;` guard ensures we do not perform unnecessary operations once the result buffer is full, providing a minor constant-time optimization.

---

## 4. Key Insights & Considerations

*   **Memory Overhead of `ArrayList`:** While $O(n)$ in complexity, Java’s `ArrayList` incurs object metadata overhead. For extremely large datasets with high cardinality, initializing thousands of `ArrayList` instances can lead to heap pressure. If memory is a hard constraint, consider a primitive-based linked-list approach using two arrays: `next[n]` and `head[n+1]`.
*   **The "All Unique" Edge Case:** If all elements are unique, each list in `freq` will contain exactly one item. The logic holds, and performance remains linear, proving the robustness of the distribution sort.
*   **Performance vs. Heap:** A common alternative is using a `PriorityQueue` (Min-Heap) of size $k$. That approach is $O(n \log k)$. This Bucket Sort approach is theoretically superior for large $n$. However, it requires $O(n)$ extra space, whereas a Min-Heap approach can technically be optimized to $O(k)$ space if the counting is done via a stream or external storage, though usually, the counting phase dominates space regardless.
*   **Compiler/Runtime Note:** Java does not support direct generic array creation (`new List<Integer>[n+1]`) due to type erasure. The code uses `new List[n+1]` which triggers an unchecked warning, but is standard practice in Java for this pattern. Ensure `List` is raw-typed to avoid compile-time errors.

---
