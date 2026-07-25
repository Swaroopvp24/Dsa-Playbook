class Solution {
    public int maxSubArray(int[] nums) {
        int max = -10001, sum = 0;
        int st = -1, ansend = -1, ansst = -1; //varaibles used dto help to find the subaarray start and end points
        for (int i = 0; i < nums.length; i++) {
            if (sum == 0) {
                st = i;
            }
            sum += nums[i];
            if (sum > max) {
                max = sum;
                ansst = st;
                ansend = i;
            }
            if (sum < 0) {
                sum = 0;
            }
        }
        for (int i = ansst; i <= ansend; i++) {
            System.out.print(nums[i] + " ");
        }//This if for printing the subarray wit largest sum
        return max;
    }
}