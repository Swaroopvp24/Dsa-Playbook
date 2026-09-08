class Solution {
    public int findMaxConsecutiveOnes(int[] nums) {
        int ct = 0, maxCt = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == 1) {
                ct++;
                maxCt = Math.max(ct, maxCt);
            } else {
                ct = 0;
            }
        }
        return maxCt;
    }
}