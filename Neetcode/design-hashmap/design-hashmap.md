# design-hashmap

## usinga integer arrray.java
*Style: concise*

### MyHashMap Notes

**Overview**
Implements a basic hash map using a fixed-size integer array to achieve $O(1)$ time complexity for all operations. It utilizes the key directly as the array index.

**Key Components**
*   **`map` (int[])**: A pre-allocated array of size 1,000,001. Stores values at the index corresponding to the key.
*   **`put(key, value)`**: Assigns the value to the specified index.
*   **`get(key)`**: Returns the value at the index; returns -1 if empty.
*   **`remove(key)`**: Resets the value at the index to -1, effectively marking it as deleted.

**Logic Notes**
*   **Space-Time Tradeoff**: Sacrifices significant memory (1MB array) to avoid collision handling (chaining or open addressing) and achieve constant time performance.
*   **Initialization**: `Arrays.fill(map, -1)` is critical because default `int` array values are 0, which would conflict with valid map values.
*   **Constraint Dependency**: This implementation is only viable because the problem constraints limit keys to the range $[0, 10^6]$.

---
