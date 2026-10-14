class Solution {
    public int countConsistentStrings(String allowed, String[] words) {
        boolean[] allowedCharacters = new boolean[26];

        // Mark each allowed character as true.
        for (char character : allowed.toCharArray()) {
            allowedCharacters[character - 'a'] = true;
        }

        int consistentCount = 0;

        // Check whether each word contains only allowed characters.
        for (String word : words) {
            boolean isConsistent = true;

            for (char character : word.toCharArray()) {
                if (!allowedCharacters[character - 'a']) {
                    isConsistent = false;
                    break; // Stop checking if a disallowed character is found.
                }
            }

            // Count the word if all its characters are allowed.
            if (isConsistent) {
                consistentCount++;
            }
        }

        return consistentCount;
    }
}