class Solution {
    public String minWindow(String s, String t) {
        if (t.length() > s.length() || t.isEmpty()) {
            return "";
        }

        int[] requiredCount = new int[128];
        int[] windowCount = new int[128];

        int requiredCharacters = 0;
        int formedCharacters = 0;

        // Count required characters in t
        for (char ch : t.toCharArray()) {
            if (requiredCount[ch] == 0) {
                requiredCharacters++;
            }
            requiredCount[ch]++;
        }

        int left = 0;
        int minWindowStart = 0;
        int minWindowLength = Integer.MAX_VALUE;

        // Expand the window using right pointer
        for (int right = 0; right < s.length(); right++) {
            char currentChar = s.charAt(right);
            windowCount[currentChar]++;

            // This character has now reached its required frequency
            if (windowCount[currentChar] == requiredCount[currentChar]) {
                formedCharacters++;
            }

            // Shrink the window while it remains valid
            while (formedCharacters == requiredCharacters) {
                int currentWindowLength = right - left + 1;

                if (currentWindowLength < minWindowLength) {
                    minWindowLength = currentWindowLength;
                    minWindowStart = left;
                }

                char leftChar = s.charAt(left);
                windowCount[leftChar]--;

                if (windowCount[leftChar] < requiredCount[leftChar]) {
                    formedCharacters--;
                }

                left++;
            }
        }

        return minWindowLength == Integer.MAX_VALUE
                ? ""
                : s.substring(minWindowStart, minWindowStart + minWindowLength);
    }
}