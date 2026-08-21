class Solution {
    public boolean checkInclusion(String s1, String s2) {
        int[] targetFreq = new int[26];
        int[] windowFreq = new int[26];
        int windowSize = s1.length();
        if (windowSize > s2.length())
            return false;

        // Build frequency arrays for s1 and the first window
        for (int i = 0; i < s1.length(); i++) {
            targetFreq[s1.charAt(i) - 'a']++;
            windowFreq[s2.charAt(i) - 'a']++;
        }

        // Check the first window
        if (Arrays.equals(targetFreq, windowFreq)) {
            return true;
        }

        int left = 0;

        for (int right = windowSize; right < s2.length(); right++) {
            // Keep the window size equal to s1
            while (right - left >= windowSize) {
                windowFreq[s2.charAt(left) - 'a']--;
                left++;
            }

            // Add the new character to the window
            windowFreq[s2.charAt(right) - 'a']++;

            // Check if current window is a permutation of s1
            if (Arrays.equals(targetFreq, windowFreq)) {
                return true;
            }
        }

        return false;
    }
}