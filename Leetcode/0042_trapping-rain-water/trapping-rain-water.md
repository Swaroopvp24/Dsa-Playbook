# trapping-rain-water

## standard_two_pointer.java
*Style: concise*

### Trapping Rain Water (Two-Pointer)

**Overview**
Calculates the total volume of trapped water between bars of varying heights using a two-pointer approach. It achieves $O(n)$ time complexity and $O(1)$ space complexity by processing from both ends toward the center.

**Key Components**
*   **`l`, `r`**: Pointers tracking the left and right boundaries.
*   **`lmax`, `rmax`**: Track the highest bars encountered so far from the left and right, respectively.
*   **`total`**: Accumulator for the water volume.

**Non-Obvious Logic**
*   **The Bottleneck Principle**: The amount of water at any index is determined by `min(lmax, rmax) - height[i]`. 
*   **Pointer Advancement**: By always moving the pointer pointing to the *smaller* height between `lmax` and `rmax`, we guarantee that the current side's boundary is the true bottleneck for that position, making the `min(lmax, rmax)` calculation implicitly accurate without needing a pre-computed prefix/suffix array.
*   **Safety**: The logic works even if `l == r` because the `height[l] < height[r]` comparison handles the final center element correctly.

---
