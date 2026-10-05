class Solution {
    public int numIdenticalPairs(int[] nums) {
        HashMap<Integer, Integer> frequencyMap = new HashMap<>();
        int goodPairs = 0;

        for (int number : nums) {
            // Each previous occurrence forms a new pair with this number.
            int previousCount = frequencyMap.getOrDefault(number, 0);
            goodPairs += previousCount;

            // Record the current occurrence.
            frequencyMap.put(number, previousCount + 1);
        }

        return goodPairs;
    }
}
