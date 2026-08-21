class Solution {
    public boolean checkInclusion(String s1, String s2) {
        int[] targetFreq = new int[26];
        int[] windowFreq = new int[26];
        int windowSize = s1.length();

        if (windowSize > s2.length()) {
            return false;
        }

        // Frequency of s1 and // Build the first window of size k
        for (int i = 0; i < windowSize; i++) {
            targetFreq[s1.charAt(i) - 'a']++;
            windowFreq[s2.charAt(i) - 'a']++;
        }

        // Count how many characters currently match
        int matchingChars = 0;
        for (int i = 0; i < 26; i++) {
            if (targetFreq[i] == windowFreq[i]) {
                matchingChars++;
            }
        }

        int left = 0;

        for (int right = windowSize; right < s2.length(); right++) {
            // Current window matches s1
            if (matchingChars == 26) {
                return true;
            }

            // Character ENTERS the window
            int charIndex = s2.charAt(right) - 'a';
            windowFreq[charIndex]++;

            // After adding:
            // If the frequency now matches s1's frequency,
            // we just CREATED a match.
            if (targetFreq[charIndex] == windowFreq[charIndex]) {
                matchingChars++;
            }
            // If the frequency is now one MORE than s1's frequency,
            // it means it WAS matching before, but we just DESTROYED the match.
            else if (targetFreq[charIndex] + 1 == windowFreq[charIndex]) {
                matchingChars--;
            }

            // Character LEAVES the window
            charIndex = s2.charAt(left) - 'a';
            windowFreq[charIndex]--;

            // After removing:
            // If the frequency now matches s1's frequency,
            // we just CREATED a match.
            if (targetFreq[charIndex] == windowFreq[charIndex]) {
                matchingChars++;
            }
            // If the frequency is now one LESS than s1's frequency,
            // it means it WAS matching before, but we just DESTROYED the match.
            else if (targetFreq[charIndex] - 1 == windowFreq[charIndex]) {
                matchingChars--;
            }

            left++;
        }

        return matchingChars == 26;
    }
}