class Solution {
    public boolean isIsomorphic(String s, String t) {
        HashMap<Character, Character> sourceToTarget = new HashMap<>();
        HashMap<Character, Character> targetToSource = new HashMap<>();

        for (int i = 0; i < s.length(); i++) {
            char sourceChar = s.charAt(i);
            char targetChar = t.charAt(i);

            // Check if source character is already mapped differently
            if (sourceToTarget.containsKey(sourceChar)
                    && sourceToTarget.get(sourceChar) != targetChar) {
                return false;
            }

            // Check if target character is already mapped to a different source
            if (targetToSource.containsKey(targetChar)
                    && targetToSource.get(targetChar) != sourceChar) {
                return false;
            }

            // Store the mapping in both directions
            sourceToTarget.put(sourceChar, targetChar);
            targetToSource.put(targetChar, sourceChar);
        }

        return true;
    }
}
