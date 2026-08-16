class Solution {
    public int removeDuplicates(int[] nums) {
        int writeIndex = 1;

        for (int readIndex = 1; readIndex < nums.length; readIndex++) {
            if (nums[readIndex] != nums[readIndex - 1]) {
                nums[writeIndex++] = nums[readIndex];
            }
        }

        return writeIndex;
    }
}