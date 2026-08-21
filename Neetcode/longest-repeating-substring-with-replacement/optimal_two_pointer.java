class Solution {
    public int characterReplacement(String s, int k) {
        Map<Character, Integer> characterFrequency = new HashMap<>();

        int mostFrequentCount = 0;
        int left = 0;
        int maxLength = 0;

        for (int right = 0; right < s.length(); right++) {

            char currentCharacter = s.charAt(right);

            characterFrequency.put(
                currentCharacter,
                characterFrequency.getOrDefault(currentCharacter, 0) + 1
            );

            mostFrequentCount = Math.max(
                mostFrequentCount,
                characterFrequency.get(currentCharacter)
            );

            while ((right - left + 1) - mostFrequentCount > k) {

                char leftCharacter = s.charAt(left);

                characterFrequency.put(
                    leftCharacter,
                    characterFrequency.get(leftCharacter) - 1
                );

                left++;
            }

            maxLength = Math.max(
                maxLength,
                right - left + 1
            );
        }

        return maxLength;
    }
}