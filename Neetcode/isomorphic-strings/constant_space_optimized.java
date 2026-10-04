class Solution {
    public boolean isIsomorphic(String s, String t) {
        int[] sourceToTarget = new int[256];
        int[] targetToSource = new int[256];

        for (int i = 0; i < s.length(); i++) {
            char sourceChar = s.charAt(i);
            char targetChar = t.charAt(i);

            // Check source -> target mapping
            if (sourceToTarget[sourceChar] != 0
                    && sourceToTarget[sourceChar] != targetChar) {
                return false;
            }

            // Check target -> source mapping
            if (targetToSource[targetChar] != 0
                    && targetToSource[targetChar] != sourceChar) {
                return false;
            }

            // Store the mapping in both directions
            sourceToTarget[sourceChar] = targetChar;
            targetToSource[targetChar] = sourceChar;
        }

        return true;
    }
}
