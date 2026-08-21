class Solution {
    public boolean checkInclusion(String s1, String s2) {
        int[] s1f = new int[26];
        int[] freq = new int[26];
        int k = s1.length();

        if (k > s2.length()) {
            return false;
        }

        // Frequency of s1 and // Build the first window of size k
        for (int i = 0; i < k; i++) {
            s1f[s1.charAt(i) - 'a']++;
            freq[s2.charAt(i) - 'a']++;
        }

        // Count how many characters currently match
        int matches = 0;
        for (int i = 0; i < 26; i++) {
            if (s1f[i] == freq[i]) {
                matches++;
            }
        }

        int l = 0;

        for (int r = k; r < s2.length(); r++) {
            // Current window matches s1
            if (matches == 26) {
                return true;
            }

            // Character ENTERS the window
            int index = s2.charAt(r) - 'a';
            freq[index]++;

            // After adding:
            // If the frequency now matches s1's frequency,
            // we just CREATED a match.
            if (s1f[index] == freq[index]) {
                matches++;
            }
            // If the frequency is now one MORE than s1's frequency,
            // it means it WAS matching before, but we just DESTROYED the match.
            else if (s1f[index] + 1 == freq[index]) {
                matches--;
            }

            // Character LEAVES the window
            index = s2.charAt(l) - 'a';
            freq[index]--;

            // After removing:
            // If the frequency now matches s1's frequency,
            // we just CREATED a match.
            if (s1f[index] == freq[index]) {
                matches++;
            }
            // If the frequency is now one LESS than s1's frequency,
            // it means it WAS matching before, but we just DESTROYED the match.
            else if (s1f[index] - 1 == freq[index]) {
                matches--;
            }

            l++;
        }

        return matches == 26;
    }
}
