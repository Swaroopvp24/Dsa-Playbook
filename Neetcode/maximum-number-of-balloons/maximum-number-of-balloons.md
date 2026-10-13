# maximum-number-of-balloons

## attempt_1.java
*Style: detailed*

# Deep‑Dive Reference: `Solution.maxNumberOfBalloons`

---

## 1. Summary – What the algorithm does & why it works  

The method **`maxNumberOfBalloons(String text)`** computes the maximum number of times the word **“balloon”** can be assembled from the characters of `text`.  

*Key observations*  

| Letter | Required count per balloon |
|--------|----------------------------|
| **b**  | 1 |
| **a**  | 1 |
| **l**  | 2 |
| **o**  | 2 |
| **n**  | 1 |

Thus the answer is the **minimum** over the available frequencies, after accounting for the multiplicities of `l` and `o`.  

The algorithm follows the classic **frequency‑count‑then‑min‑reduce** pattern:

1. **Linear scan** to build a 26‑element integer histogram (`charFrequency`) for lower‑case English letters.  
2. **Constant‑time** look‑ups of the needed letters, adjusting the counts of `l` and `o` by dividing by 2.  
3. Return the smallest of those values – the bottleneck resource that determines how many whole “balloon” strings can be formed.

The solution is **deterministic**, **O(n)** in the length of the input, and uses **O(1)** auxiliary space (the fixed‑size array).  

---

## 2. Complexity Analysis  

| Aspect | Detail |
|--------|--------|
| **Time** | `O(|text|)` – a single pass over the character array (`text.toCharArray()`). All subsequent operations are constant‑time arithmetic/comparisons. |
| **Space** | `O(1)` – the `int[26]` frequency table occupies a fixed amount of memory regardless of input size. No recursion or additional collections are allocated. |
| **Why** | The loop iterates exactly `text.length()` times, each iteration does **O(1)** work (index calculation `ch - 'a'` and increment). The `Math.min` chain runs a constant 5 times, independent of `|text|`. No extra structures grow with input size, hence constant space. |
| **Potential hidden costs** | `String.toCharArray()` creates a new `char[]` copy of the string (cost `O(|text|)` memory temporarily). If memory‑critical, one could iterate over `text.charAt(i)` instead, saving the temporary array. |

---

## 3. Component Deep Dive  

### 3.1 Data Structure – `int[] charFrequency = new int[26]`

* **Purpose** – Holds the count of each lowercase alphabet character.  
* **Indexing** – `ch - 'a'` maps `'a'` → 0, `'b'` → 1, …, `'z'` → 25.  
* **Assumption** – Input consists solely of lowercase English letters. If the contract is violated (e.g., uppercase letters, digits, or Unicode symbols), the index calculation may produce a negative or out‑of‑bounds value, causing `ArrayIndexOutOfBoundsException`.  

### 3.2 Frequency Counting Loop  

```java
for (char ch : text.toCharArray()) {
    charFrequency[ch - 'a']++;
}
```

* **`text.toCharArray()`** – Copies the backing character data; O(n) time + O(n) temporary memory.  
* **`charFrequency[ch - 'a']++`** – Direct array increment – fastest possible count update in Java.  
* **Edge‑case handling** –  
  * Empty `text` → loop never executes, frequencies stay zero.  
  * Very long strings (e.g., > 2^31‑1 characters) are impossible due to Java `String` length limit (`int`).  

### 3.3 Computing the bottleneck – the `Math.min` cascade  

```java
int maxBalloons = Integer.MAX_VALUE;

// single‑occurrence letters
maxBalloons = Math.min(maxBalloons, charFrequency['b' - 'a']);
maxBalloons = Math.min(maxBalloons, charFrequency['a' - 'a']);
maxBalloids = Math.min(maxBalloons, charFrequency['n' - 'a']);

// double‑occurrence letters (need integer division)
maxBalloons = Math.min(maxBalloons, charFrequency['l' - 'a'] / 2);
maxBalloons = Math.min(maxBalloons, charFrequency['o' - 'a'] / 2);
```

* **Why start with `Integer.MAX_VALUE`?** Guarantees the first `Math.min` will simply copy the first relevant frequency, establishing a correct baseline.  
* **Division for `l` and `o`** – Integer division automatically floors the result, which is exactly the number of *pairs* we can extract.  
* **Order independence** – The chain could be rearranged; the final result is the minimum across all constraints.  

### 3.4 Return  

`return maxBalloons;` – The variable now holds the maximum count of whole “balloon” words that can be assembled.  

---

## 4. Key Insights & Gotchas  

| Area | Insight / Pitfall | Mitigation / Recommendation |
|------|-------------------|-----------------------------|
| **Input validation** | The method assumes only `'a'`‑`'z'`. Upper‑case letters (`'A'`) produce negative indices (`'A' - 'a' = -32`). | Either document the pre‑condition clearly or add a guard: `if (ch < 'a' || ch > 'z') continue;` (or `Character.toLowerCase`). |
| **Overflow** | The frequency array uses `int`. In pathological cases (`text` length ≈ 2 147 483 647) an individual bucket could overflow `int` when incremented. In practice, Java strings cannot exceed `Integer.MAX_VALUE` characters, so overflow is *theoretically* possible but practically improbable. | Use `long` if you anticipate near‑max length inputs, or guard with `if (charFrequency[idx] == Integer.MAX_VALUE) continue;`. |
| **`toCharArray` allocation** | For extremely large strings, the temporary `char[]` doubles memory pressure. | Replace with `for (int i = 0; i < text.length(); i++) { char ch = text.charAt(i); … }`. |
| **Repeated `Math.min`** | The cascade is readable but performs 5 separate method calls. In a hot‑path micro‑benchmark, a single loop over a small constant array could be marginally faster. | Example alternative: `int[] needed = {freq['b'], freq['a'], freq['n'], freq['l']/2, freq['o']/2}; for (int v : needed) maxBalloons = Math.min(maxBalloons, v);` – negligible in practice. |
| **Extensibility** | If the target word changes (e.g., “hello”), the current hard‑coded approach would require editing many lines. | Generalize: build a map `Map<Character, Integer> required = Map.of('b',1,'a',1,'l',2,'o',2,'n',1);` then iterate over the entry set, applying `freq[ch] / need`. This adds O(k) extra work where k = distinct letters in target, still O(1) for fixed words. |
| **Thread‑safety** | No static mutable state → method is thread‑safe. | No action needed. |
| **Testing edge cases** | *Empty string* → returns 0 (correct).<br>*Only one ‘l’* → `charFrequency['l']/2` yields 0, result 0.<br>*Huge count of a single letter* (e.g., many ‘l’s but no other letters) → correctly limited by missing letters. | Include unit tests covering: empty, single‑letter strings, strings with excess of double‑required letters, strings containing non‑alphabetic characters (to confirm behavior). |
| **Alternative algorithms** | The problem is essentially a **resource allocation** / **integer division** problem. The chosen solution is optimal; any algorithm with more passes (e.g., sorting) would be strictly worse. | No alternative needed; the current linear‑time solution is optimal. |
| **Complexity hidden in `Math.min`** | `Math.min` is an inlined static method, compiled to a simple branch. No hidden recursion. | No impact. |
| **Potential micro‑optimizations** | Inline the `Math.min` logic manually (`if (candidate < maxBalloons) maxBalloons = candidate;`) may shave a few nanoseconds in tight loops. | Unnecessary unless the method is invoked billions of times. |
| **Naming** | `maxBalloons` actually stores the **minimum** feasible count. Naming could be `maxPossible`. | Minor readability improvement. |

---

## 5. Full Annotated Source (for reference)

```java
class Solution {
    /**
     * Returns the maximum number of times the word "balloon" can be formed
     * from the characters of the supplied string.
     *
     * Preconditions:
     *   - {@code text} contains only lower‑case English letters 'a'..'z'.
     *
     * @param text input string
     * @return maximum count of "balloon" that can be assembled
     */
    public int maxNumberOfBalloons(String text) {
        // 26‑slot histogram for 'a'..'z'
        int[] charFrequency = new int[26];

        // ---------- 1️⃣ Frequency counting ----------
        // toCharArray() creates a copy; could be avoided with charAt(i)
        for (char ch : text.toCharArray()) {
            // Assuming ch is within 'a'..'z'
            charFrequency[ch - 'a']++;
        }

        // ---------- 2️⃣ Determine bottleneck ----------
        // Start with an "infinite" upper bound
        int maxBalloons = Integer.MAX_VALUE;

        // Single‑use letters
        maxBalloons = Math.min(maxBalloons, charFrequency['b' - 'a']);
        maxBalloons = Math.min(maxBalloons, charFrequency['a' - 'a']);
        maxBalloons = Math.min(maxBalloons, charFrequency['n' - 'a']);

        // Letters needed twice per balloon: integer division floors the pair count
        maxBalloons = Math.min(maxBalloons, charFrequency['l' - 'a'] / 2);
        maxBalloons = Math.min(maxBalloons, charFrequency['o' - 'a'] / 2);

        // ---------- 3️⃣ Return result ----------
        return maxBalloons; // 0 if any required letter is missing
    }
}
```

---

## 6. Quick Reference Cheat‑Sheet  

| Step | Operation | Complexity |
|------|-----------|------------|
| 1 | Build 26‑size frequency array | `O(n)` time, `O(1)` space |
| 2 | Compute `min(freq['b'], freq['a'], freq['n'], freq['l']/2, freq['o']/2)` | `O(1)` time |
| 3 | Return result | `O(1)` time |

Overall: **`O(n)` time, `O(1)` extra space**.  

--- 

*Prepared by Senior Staff Engineer – Deep technical audit of the “maxNumberOfBalloons” implementation.*

---
