class Solution {
    public List<Integer> findDisappearedNumbers(int[] nums) {
        // Mark the index corresponding to each number as visited.
        for (int currentNumber : nums) {
            int index = Math.abs(currentNumber) - 1;
            nums[index] = -Math.abs(nums[index]);
        }

        List<Integer> missingNumbers = new ArrayList<>();

        // Positive values indicate that the corresponding number was missing.
        for (int index = 0; index < nums.length; index++) {
            if (nums[index] > 0) {
                missingNumbers.add(index + 1);
            }
        }

        return missingNumbers;
    }
}
