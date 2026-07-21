# sort-colors

## attempt_1.java
*Style: concise*

### Study Notes: Sort Colors (Counting Sort Approach)

**Purpose**
This code implements a two-pass counting sort to organize an array containing only three distinct integers (0, 1, and 2) in-place with $O(n)$ time complexity and $O(1)$ auxiliary space.

**Key Components**
*   **`freq[]`**: An integer array of size 3 used as a frequency map to store the count of 0s, 1s, and 2s present in the input.
*   **First Pass**: Iterates through the input to populate the `freq` counts.
*   **Second Pass**: Overwrites the original array by iterating through the `freq` map and placing the corresponding values back into the array in order.

**Non-Obvious Logic**
*   **Space/Time Efficiency**: While this uses two passes, it is more space-efficient than allocating a new array. 
*   **Overwriting Strategy**: The use of a single pointer `j` to track the current index in `nums` while iterating through the nested `freq` loop allows for efficient re-population of the array regardless of the number of elements per color.

---
