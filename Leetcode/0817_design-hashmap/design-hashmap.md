# design-hashmap

## using_a_integer_array.java
*Style: detailed*

# Technical Deep-Dive: Direct-Address Table Implementation of `MyHashMap`

## Summary
The provided `MyHashMap` implementation utilizes a **Direct-Address Table** (specifically a dense array) to map integer keys to their corresponding values. By treating the key as a direct index into a pre-allocated primitive array, the implementation achieves $O(1)$ constant-time operations for `put`, `get`, and `remove`. This approach circumvents the overhead of hashing functions, collision resolution strategies (like chaining or open addressing), and dynamic resizing, assuming the input key space is bounded and relatively small.

## Complexity Analysis

### Time Complexity
*   **`put(int key, int value)`**: **$O(1)$**. Array index access in Java is a primitive pointer offset calculation, resulting in constant-time complexity.
*   **`get(int key)`**: **$O(1)$**. Direct index lookup without search overhead.
*   **`remove(int key)`**: **$O(1)$**. Simple assignment of a sentinel value.

### Space Complexity
*   **Total Space**: **$O(N)$**, where $N$ is the size of the key space ($1,000,001$).
*   **Reasoning**: The implementation allocates a contiguous block of memory on the heap equal to the maximum defined range. In this case, the heap footprint is $1,000,001 \times 4$ bytes (for `int` primitives), resulting in approximately 4MB of fixed memory usage regardless of how many entries are actually stored.

## Component Deep Dive

### 1. Data Structure Choice
The use of `int[]` is a design choice favoring speed and simplicity over memory efficiency. By mapping the key directly to the index, the implementation avoids the "Birthday Paradox" and the clustering issues found in standard `java.util.HashMap` implementations.

### 2. Sentinel Value Strategy
The code uses `-1` as a sentinel value to represent "empty" or "absent." 
*   **The Implicit Contract**: This design implicitly restricts the `value` range. The `put` operation effectively ignores the distinction between an uninitialized slot and a slot containing `-1`.
*   **Risk**: If the business logic requires storing `-1` as a valid user-defined value, this implementation will fail to distinguish between "not found" and "value is -1."

### 3. Allocation Strategy
The constructor utilizes `Arrays.fill(map, -1)`. 
*   **Performance Note**: While this ensures deterministic behavior, it forces a full sweep of the array upon instantiation. In a lazy-loading scenario or a sparse-data scenario, this is an $O(N)$ penalty that may cause latency spikes during object creation.

## Key Insights

### Performance Nuances
*   **Cache Locality**: Because the map is a flat array, it is extremely cache-friendly. Modern CPUs will benefit from spatial locality during sequential access patterns, significantly outperforming hash-based implementations that involve linked-list node traversal (pointer chasing).
*   **Memory Overhead**: The primary bottleneck is the **fixed memory footprint**. If the key range were to grow to `Integer.MAX_VALUE`, this approach would trigger an `OutOfMemoryError` or exceed hardware constraints.

### Subtle Bugs & Limitations
*   **Input Range Constraints**: The solution is strictly bound to keys $[0, 1000000]$. Any attempt to use a negative key or a key $> 1000000$ will trigger an `ArrayIndexOutOfBoundsException`. A more robust implementation would require validation logic or a modulo-based hashing strategy.
*   **Memory Waste**: In a sparse data scenario—where perhaps only 10 keys are stored—the application still occupies 4MB of heap. This is an inefficient use of resources compared to a `List` of buckets or a `TreeMap`.
*   **Integer Overflow**: If this class were adapted for larger ranges, simple array indexing would require `long` indices, which are not supported by standard Java arrays, necessitating a transition to a true hash-map or a segmented array structure.

### Recommendations for Production-Grade Refinement
If moving beyond this specific algorithmic challenge, consider:
1.  **Bitsets/Optional**: Using an auxiliary `BitSet` to track "occupied" status if `-1` is a valid payload value.
2.  **Chaining**: Implementing a linked-list approach at each index (Bucketing) to support an infinite range of keys while keeping memory usage proportional to the number of *stored* elements rather than the *range* of keys.

---
