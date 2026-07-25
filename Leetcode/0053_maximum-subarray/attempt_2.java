class Solution {
    public int maxSubArray(int[] nums) {
        int maxSubArray = 0;
        int ans = nums[0];


        for (int i=0;i<nums.length;i++) {
            maxSubArray += nums[i];
            if (maxSubArray < 0) {
                maxSubArray = 0;
                ans= Math.max(ans, nums[i]);
            }
            else ans = Math.max(ans, maxSubArray);
        }

        return ans;
    }
}