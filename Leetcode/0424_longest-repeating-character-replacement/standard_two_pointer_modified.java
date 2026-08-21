public class Solution {
    public int characterReplacement(String s, int k) {
        int maxLength = 0;

        boolean[] present = new boolean[26];

        // Mark characters that actually appear in the string
        for (char character : s.toCharArray()) {
            present[character - 'A'] = true;
        }

        // Try each character as the target character
        for (char targetCharacter = 'A'; targetCharacter <= 'Z'; targetCharacter++) {

            if (!present[targetCharacter - 'A']) {
                continue;
            }

            int targetCharacterCount = 0;
            int left = 0;

            for (int right = 0; right < s.length(); right++) {

                if (s.charAt(right) == targetCharacter) {
                    targetCharacterCount++;
                }

                while ((right - left + 1) - targetCharacterCount > k) {

                    if (s.charAt(left) == targetCharacter) {
                        targetCharacterCount--;
                    }

                    left++;
                }

                maxLength = Math.max(
                    maxLength,
                    right - left + 1
                );
            }
        }

        return maxLength;
    }
}