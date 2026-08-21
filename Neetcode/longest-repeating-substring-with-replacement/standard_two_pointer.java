public class Solution {
    public int characterReplacement(String s, int k) {
        int maxLength = 0;

        // Get all unique characters
        HashSet<Character> uniqueCharacters = new HashSet<>();

        for (char character : s.toCharArray()) {
            uniqueCharacters.add(character);
        }

        // Try making each character the repeating character
        for (char targetCharacter : uniqueCharacters) {
            int targetCharacterCount = 0;
            int left = 0;

            for (int right = 0; right < s.length(); right++) {
                // Include s[right] in our window
                if (s.charAt(right) == targetCharacter) {
                    targetCharacterCount++;
                }

                // If we need too many replacements,
                // shrink the window from the left
                while ((right - left + 1) - targetCharacterCount > k) {
                    if (s.charAt(left) == targetCharacter) {
                        targetCharacterCount--;
                    }

                    left++;
                }

                maxLength = Math.max(maxLength, right - left + 1);
            }
        }

        return maxLength;
    }
}