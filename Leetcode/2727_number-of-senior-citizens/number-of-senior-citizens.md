# number-of-senior-citizens

## attempt_1.java
*Style: detailed*

# Technical Reference: Passenger Age Categorization

## 1. Summary
The `countSeniors` method implements a targeted data extraction strategy to identify passengers over the age of 60 from a formatted string array. The approach utilizes **O(1) positional slicing** on fixed-width character sequences. By leveraging the invariant that passenger details follow a strict schema (where age always resides at indices 11 and 12), the algorithm avoids complex regex or heavy object deserialization, favoring direct primitive parsing for optimal cache locality and minimal heap allocation.

## 2. Complexity Analysis

### Time Complexity: $O(N)$
*   **$N$**: Total number of strings in the `details` array.
*   **Reasoning**: The algorithm performs a single linear pass over the input array. For each string of length $L$, the `substring` and `Integer.parseInt` operations are $O(1)$ because the slice size (2 characters) and the maximum integer value (99) are constant constraints. Thus, the operation is effectively $O(N \times C)$, where $C$ is a constant.

### Space Complexity: $O(1)$
*   **Reasoning**: The algorithm operates in-place relative to the input array. It uses a single integer primitive (`ct`) for state tracking. While `substring` creates a new `String` object in many JVM implementations (post-Java 7u6), the short-lived nature of these objects allows for efficient Young Generation GC, maintaining a constant auxiliary space footprint.

## 3. Component Deep Dive

### Positional Extraction (`s.substring(11, 13)`)
*   **Schema Dependency**: The implementation assumes a strict contract: the age field is immutable at indices `[11, 12]`. Any deviation in the input format (e.g., variable-length leading fields) would necessitate a transition from positional indexing to a delimiter-based parser (like `String.split()` or `StringTokenizer`).
*   **Efficiency**: `substring` in modern OpenJDK/HotSpot performs a `char[]` copy (or `byte[]` in Compact Strings). Since the target is only 2 characters, the overhead is negligible.

### Parsing (`Integer.parseInt`)
*   **Mechanism**: The method iterates through the 2-character array, applying radix-10 arithmetic.
*   **Edge Case Handling**: 
    *   **Non-numeric data**: `Integer.parseInt` will throw a `NumberFormatException` if indices 11-12 contain non-digit characters. 
    *   **Leading Zeros**: `Integer.parseInt` natively handles leading zeros (e.g., "09"), ensuring the age logic remains robust for younger passengers.

## 4. Key Insights

*   **Fixed-Width Optimization**: The primary performance driver here is the avoidance of heap-heavy operations. In systems requiring high-throughput processing of millions of passenger records, replacing `substring` and `parseInt` with a custom character-offset calculation would yield further micro-optimizations:
    ```java
    // Theoretical zero-allocation alternative
    int age = (s.charAt(11) - '0') * 10 + (s.charAt(12) - '0');
    ```
    *This bypasses string object creation and `NumberFormatException` parsing overhead entirely.*

*   **Subtle Bug Surface**:
    *   **String Length**: The code lacks a precondition check for `s.length()`. If a malformed string with length < 13 is passed, an `IndexOutOfBoundsException` will trigger. In a production environment, an input validation layer or a `length >= 13` guard clause is recommended.
    *   **Locale Sensitivity**: While `Integer.parseInt` is generally robust, it is the standard approach for ASCII digits. It does not handle Unicode digits that might appear in internationalized data, though per typical competitive programming constraints, ASCII is assumed.

*   **Memory Pressure**: Since this is a simple primitive-based counter, the main bottleneck is the `String` allocation. If this were to be ported to a system with extreme GC pressure, consuming the input as `byte[]` arrays instead of `String[]` would allow for zero-copy parsing using the byte-offset equivalent of the math shown in the Key Insights section.

---
