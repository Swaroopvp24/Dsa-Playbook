# unique-email-addresses

## attempt_1.java
*Style: detailed*

# Engineering Deep Dive: Email Normalization & De-duplication

## 1. Summary
The provided implementation solves the email unique-count problem by transforming raw email strings into a canonical representation. It utilizes a **HashSet** to achieve O(1) average time complexity for duplicate detection. The algorithmic approach follows a "Normalize-then-Insert" pattern: 
1. **Partitioning**: Split the address at the `@` boundary.
2. **Local Name Sanitization**: Apply two specific business rules—ignoring content post-`+` and stripping all `.` characters.
3. **Canonicalization**: Reconstruct the string and persist the result into a hash-based set to enforce uniqueness.

## 2. Complexity Analysis

### Time Complexity: $O(N \cdot M)$
*   **$N$**: Number of email addresses.
*   **$M$**: Maximum length of an email string.
*   **Why**: We iterate through each email string once. Inside the loop, the `split`, `replace`, and `String` concatenation operations are linear with respect to the length of the string ($M$). Since the `HashSet.add()` operation is $O(1)$ on average (assuming a good hash distribution), the total runtime scales linearly with the total number of characters across all input emails.

### Space Complexity: $O(N \cdot M)$
*   **Why**: In the worst-case scenario where all emails are unique, the `HashSet` will store a normalized version of every input string. We allocate space proportional to the sum of the lengths of all unique normalized email strings.

## 3. Component Deep Dive

### String Normalization Logic
*   **Delimiter Splitting (`split("@")`)**: This is the primary partitioning step. It assumes a valid email format per the constraints. If the input allows malformed emails (e.g., missing `@`), an `ArrayIndexOutOfBoundsException` would be thrown, which is a potential fragility point.
*   **The `+` Rule (`split("\\+")[0]`)**: The use of `split("\\+")` is necessary because `+` is a regex metacharacter. While effective, it creates an intermediate array for every email, which is slightly suboptimal in terms of memory allocation. A `localName.indexOf('+')` check combined with `substring` would be more memory-efficient as it avoids regex compilation and array allocation.
*   **Dot Stripping (`replace(".", "")`)**: This method performs a global search-and-replace of literal `.` characters. Because `String` objects are immutable in Java, every call to `replace` (and `split`) generates new `String` objects, increasing GC pressure in high-throughput environments.

### Data Structure Selection
*   **`HashSet<String>`**: An ideal choice for $O(1)$ lookup and insertion. It relies on the `String.hashCode()` implementation, which caches the hash value after the first calculation, making subsequent operations efficient.

## 4. Key Insights & Optimization Nuances

*   **Regex Overhead**: The call to `split("\\+")` is relatively expensive because it invokes the regex engine. In a high-performance system processing millions of emails per second, replacing this with a manual character scan (iterating until `+` or `@`) would significantly reduce CPU cycles and allocations.
*   **Garbage Collection Impact**: Given that `split` and `replace` create multiple intermediate `String` objects, this implementation is heavy on the heap. For memory-constrained environments, using a `StringBuilder` or manual buffer manipulation to construct the canonical string would prevent the creation of these "throwaway" strings.
*   **Edge Case Vulnerability**:
    *   **Empty strings**: If an email is `""`, `emailParts` will have length 1, causing an `ArrayIndexOutOfBoundsException` at `emailParts[1]`.
    *   **Invalid domains**: Emails like `a@b@c` are not handled; `split("@")` would yield three parts, and `emailParts[1]` would only take the second segment, potentially leading to incorrect grouping.
*   **Potential Optimization**: If we pre-size the `HashSet` to `emails.length`, we can avoid internal resizing and re-hashing of the set as it grows, providing a minor performance boost for large inputs.

```java
// Optimization snippet for reference:
int plusIdx = localName.indexOf('+');
if (plusIdx != -1) localName = localName.substring(0, plusIdx);
localName = localName.replace(".", ""); 
```
*This snippet avoids regex overhead and provides cleaner, explicit bounds handling.*

---
