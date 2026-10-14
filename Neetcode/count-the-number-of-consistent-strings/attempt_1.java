class Solution {
    public int countConsistentStrings(String allowed, String[] words) {
        Set<Character> allowedCharacters = new HashSet<>();

        // Store all allowed characters in a set for quick lookup.
        for (char character : allowed.toCharArray()) {
            allowedCharacters.add(character);
        }

        int consistentCount = 0;

        // Check each word to see if all its characters are allowed.
        for (String word : words) {
            boolean isConsistent = true;

            for (char character : word.toCharArray()) {
                if (!allowedCharacters.contains(character)) {
                    isConsistent = false;
                    break; // Stop checking once a disallowed character is found.
                }
            }

            // Count the word only if every character is allowed.
            if (isConsistent) {
                consistentCount++;
            }
        }

        return consistentCount;
    }
}